package sf.l2j.loginserver;

import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import sf.l2j.commons.mmocore.IAcceptFilter;
import sf.l2j.commons.mmocore.IClientFactory;
import sf.l2j.commons.mmocore.IMMOExecutor;
import sf.l2j.commons.mmocore.MMOConnection;
import sf.l2j.commons.mmocore.ReceivablePacket;

import sf.l2j.loginserver.network.LoginClient;
import sf.l2j.loginserver.network.serverpackets.Init;
import sf.l2j.util.IPv4Filter;

public class SelectorHelper implements IMMOExecutor<LoginClient>, IClientFactory<LoginClient>, IAcceptFilter {

	private final ThreadPoolExecutor _generalPacketsThreadPool;

	private final IPv4Filter _ipv4filter;

	public SelectorHelper() {
		_generalPacketsThreadPool = new ThreadPoolExecutor(4, 6, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		_ipv4filter = new IPv4Filter();
	}

	@Override
	public void execute(ReceivablePacket<LoginClient> packet) {
		_generalPacketsThreadPool.execute(packet);
	}

	@Override
	public LoginClient create(MMOConnection<LoginClient> con) {
		LoginClient client = new LoginClient(con);
		client.sendPacket(new Init(client));
		return client;
	}

	@Override
	public boolean accept(SocketChannel sc) {
		return _ipv4filter.accept(sc) && !LoginController.getInstance().isBannedAddress(sc.socket().getInetAddress());
	}
}
