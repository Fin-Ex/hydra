package sf.l2j.loginserver;

import sf.l2j.commons.mmocore.AcceptFilter;
import sf.l2j.commons.mmocore.IClientFactory;
import sf.l2j.commons.mmocore.IMMOExecutor;
import sf.l2j.commons.mmocore.MMOConnection;
import sf.l2j.commons.mmocore.ReceivablePacket;
import sf.l2j.loginserver.network.LoginClient;
import sf.l2j.loginserver.network.serverpackets.Init;

import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SelectorHelper implements IMMOExecutor<LoginClient>, IClientFactory<LoginClient>, AcceptFilter {

	private final ThreadPoolExecutor _generalPacketsThreadPool;

	public SelectorHelper() {
		_generalPacketsThreadPool = new ThreadPoolExecutor(4, 6, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

	@Override
	public void execute(ReceivablePacket<LoginClient> packet) {
		_generalPacketsThreadPool.execute(packet::run);
	}

	@Override
	public LoginClient create(MMOConnection<LoginClient> con) {
		LoginClient client = new LoginClient(con);
		client.sendPacket(new Init(client));
		return client;
	}

	@Override
	public boolean accept(SocketChannel sc) {
		return !LoginController.getInstance().isBannedAddress(sc.socket().getInetAddress());
	}
}
