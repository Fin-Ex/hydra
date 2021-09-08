package sf.l2j.gameserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.finex.core.events.EventBus;
import ru.finex.gs.auth.AuthServerConfiguration;
import ru.finex.gs.model.Client;
import ru.finex.gs.network.NetworkConfiguration;
import ru.finex.ws.l2.auth.model.event.ClientAuth;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.l2.network.model.event.ClientDisconnected;
import sf.l2j.commons.crypt.NewCrypt;
import sf.l2j.commons.random.Rnd;
import sf.l2j.gameserver.network.gameserverpackets.AuthRequest;
import sf.l2j.gameserver.network.gameserverpackets.BlowFishKey;
import sf.l2j.gameserver.network.gameserverpackets.ChangeAccessLevel;
import sf.l2j.gameserver.network.gameserverpackets.GameServerBasePacket;
import sf.l2j.gameserver.network.gameserverpackets.PlayerAuthRequest;
import sf.l2j.gameserver.network.gameserverpackets.PlayerInGame;
import sf.l2j.gameserver.network.gameserverpackets.PlayerLogout;
import sf.l2j.gameserver.network.gameserverpackets.ServerStatus;
import sf.l2j.gameserver.network.loginserverpackets.AuthResponse;
import sf.l2j.gameserver.network.loginserverpackets.InitLS;
import sf.l2j.gameserver.network.loginserverpackets.KickPlayer;
import sf.l2j.gameserver.network.loginserverpackets.LoginServerFail;
import sf.l2j.gameserver.network.loginserverpackets.PlayerAuthResponse;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class LoginServerThread extends Thread {

	protected static final Logger _log = LoggerFactory.getLogger(LoginServerThread.class.getName());

	private static final int REVISION = 0x0102;

	private final Map<String, Client> _clients = new ConcurrentHashMap<>();

	private int _serverId;
	private String _serverName;

	private Socket _loginSocket;
	private InputStream _in;
	private OutputStream _out;

	private NewCrypt _blowfish;
	private byte[] _blowfishKey;
	private RSAPublicKey _publicKey;

	private byte[] _hexId;

	private int _requestId;
	private int _maxPlayers;
	private int _status;

	private EventBus networkEventBus;

	@Inject
	private AuthServerConfiguration conf;

	@Inject
	private NetworkConfiguration networkConf;

	public LoginServerThread() {
		super("LoginServerThread");

		_requestId = 1;
		_hexId = generateHex(16);
		_maxPlayers = 3000;
	}

	@Inject
	public void registerListeners(@Named("Network") EventBus eventBus) {
		this.networkEventBus = eventBus;
		eventBus.subscribe()
			.cast(ClientDisconnected.class)
			.map(ClientDisconnected::getClient)
			.filter(e -> e.getLogin() != null)
			.forEach(e -> _clients.remove(e.getLogin()));
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				// Connection
				_log.info("Connecting to login on {}:{}", conf.getHostname(), conf.getPort());

				_loginSocket = new Socket(conf.getHostname(), conf.getPort());
				_in = _loginSocket.getInputStream();
				_out = new BufferedOutputStream(_loginSocket.getOutputStream());

				// init Blowfish
				_blowfishKey = generateHex(40);
				_blowfish = new NewCrypt("_;v.]05-31!|+-%xT!^[$\00");

				while (!isInterrupted()) {
					int lengthLo = _in.read();
					int lengthHi = _in.read();
					int length = lengthHi * 256 + lengthLo;

					if (lengthHi < 0) {
						break;
					}

					byte[] incoming = new byte[length - 2];

					int receivedBytes = 0;
					int newBytes = 0;
					int left = length - 2;

					while (newBytes != -1 && receivedBytes < length - 2) {
						newBytes = _in.read(incoming, receivedBytes, left);
						receivedBytes = receivedBytes + newBytes;
						left -= newBytes;
					}

					if (receivedBytes != length - 2) {
						_log.warn("Incomplete Packet is sent to the server, closing connection.");
						break;
					}

					// Decrypt if we have a key.
					final byte[] decrypt = _blowfish.decrypt(incoming);

					// Verify the checksum.
					if (!NewCrypt.verifyChecksum(decrypt)) {
						_log.warn("Incorrect packet checksum, ignoring packet.");
						break;
					}

					int packetType = decrypt[0] & 0xff;
					switch (packetType) {
						case 0x00:
							final InitLS init = new InitLS(decrypt);

							if (init.getRevision() != REVISION) {
								_log.warn("Revision mismatch between LS and GS.");
								break;
							}

							try {
								final KeyFactory kfac = KeyFactory.getInstance("RSA");
								final BigInteger modulus = new BigInteger(init.getRSAKey());
								final RSAPublicKeySpec kspec1 = new RSAPublicKeySpec(modulus, RSAKeyGenParameterSpec.F4);

								_publicKey = (RSAPublicKey) kfac.generatePublic(kspec1);
							} catch (GeneralSecurityException e) {
								_log.warn("Troubles while init the public key sent by login.");
								break;
							}

							// send the blowfish key through the rsa encryption
							sendPacket(new BlowFishKey(_blowfishKey, _publicKey));

							// now, only accept paket with the new encryption
							_blowfish = new NewCrypt(_blowfishKey);

							sendPacket(new AuthRequest(_requestId, true, _hexId, networkConf.getHostname(), networkConf.getPort(), false, _maxPlayers));
							break;

						case 0x01:
							// login will close the connection here
							final LoginServerFail lsf = new LoginServerFail(decrypt);
							_log.info("Registeration Failed: " + lsf.getReasonString());
							break;

						case 0x02:
							final AuthResponse aresp = new AuthResponse(decrypt);

							_serverId = aresp.getServerId();
							_serverName = aresp.getServerName();

							_log.info("Registered as server: [" + _serverId + "] " + _serverName);

							final ServerStatus ss = new ServerStatus();
							ss.addAttribute(ServerStatus.STATUS, /*(Config.SERVER_GMONLY) ? ServerStatus.STATUS_GM_ONLY :*/ ServerStatus.STATUS_AUTO);
							ss.addAttribute(ServerStatus.CLOCK, false);
							ss.addAttribute(ServerStatus.BRACKETS, false);
							ss.addAttribute(ServerStatus.AGE_LIMIT, false);
							ss.addAttribute(ServerStatus.TEST_SERVER, false);
							ss.addAttribute(ServerStatus.PVP_SERVER, false);
							sendPacket(ss);
							sendPacket(new PlayerInGame(Collections.emptyList()));
							break;

						case 0x03:
							final PlayerAuthResponse par = new PlayerAuthResponse(decrypt);

							final Client client = _clients.get(par.getAccount());
							if (client != null) {
								if (par.isAuthed()) {
									sendPacket(new PlayerInGame(par.getAccount()));
								}

								networkEventBus.notify(new ClientAuth(client, par.isAuthed()));
							}
							break;

						case 0x04:
							final KickPlayer kp = new KickPlayer(decrypt);
							kickPlayer(kp.getAccount());
							break;
					}
				}
			} catch (UnknownHostException e) {
			} catch (IOException e) {
				_log.info("No connection found with loginserver, next try in 10 seconds.");
			} finally {
				try {
					_loginSocket.close();
					if (isInterrupted()) {
						return;
					}
				} catch (Exception e) {
				}
			}

			// 10 seconds tempo before another try
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public void sendLogout(String account) {
		if (account == null) {
			return;
		}

		try {
			sendPacket(new PlayerLogout(account));
		} catch (IOException e) {
			_log.warn("Error while sending logout packet to login.");
		} finally {
			_clients.remove(account);
		}
	}

	public void addClient(String account, Client client) {
		final Client existingClient = _clients.putIfAbsent(account, client);
		if (existingClient == null) {
			try {
				sendPacket(new PlayerAuthRequest(client.getLogin(), ((L2GameClient) client).getSessionId()));
			} catch (IOException e) {
				_log.warn("Error while sending player auth request.");
			}
		} else {
			client.closeNow();
			existingClient.closeNow();
			_clients.remove(account);
		}
	}

	public void sendAccessLevel(String account, int level) {
		try {
			sendPacket(new ChangeAccessLevel(account, level));
		} catch (IOException e) {
		}
	}

	public void kickPlayer(String account) {
		final Client client = _clients.get(account);
		if (client != null) {
			client.closeNow();
		}
	}

	public static byte[] generateHex(int size) {
		byte[] array = new byte[size];
		Rnd.nextBytes(array);
		return array;
	}

	private void sendPacket(GameServerBasePacket sl) throws IOException {
		byte[] data = sl.getContent();
		NewCrypt.appendChecksum(data);

		data = _blowfish.crypt(data);

		int len = data.length + 2;
		synchronized (_out) // avoids tow threads writing in the mean time
		{
			_out.write(len & 0xff);
			_out.write(len >> 8 & 0xff);
			_out.write(data);
			_out.flush();
		}
	}

	public void setMaxPlayer(int maxPlayers) {
		sendServerStatus(ServerStatus.MAX_PLAYERS, maxPlayers);

		_maxPlayers = maxPlayers;
	}

	public int getMaxPlayers() {
		return _maxPlayers;
	}

	public void sendServerStatus(int id, int value) {
		try {
			final ServerStatus ss = new ServerStatus();
			ss.addAttribute(id, value);

			sendPacket(ss);
		} catch (IOException e) {
		}
	}

	public String getStatusString() {
		return ServerStatus.STATUS_STRING[_status];
	}

	public String getServerName() {
		return _serverName;
	}

	public int getServerStatus() {
		return _status;
	}

	public void setServerStatus(int status) {
		sendServerStatus(ServerStatus.STATUS, status);
		_status = status;
	}
}
