package sf.l2j.commons.mmocore;

import ru.finex.nif.NetworkClient;

/**
 * @author KenM
 */
public abstract class MMOClient implements NetworkClient {

	private final MMOConnection _con;

	public MMOClient(final MMOConnection con) {
		_con = con;
	}

	@Override
	public MMOConnection getConnection() {
		return _con;
	}

	protected abstract void onDisconnection();
	protected abstract void onForcedDisconnection();
}
