package sf.l2j.commons.mmocore;

import ru.finex.nif.NetworkPacket;

import java.nio.ByteBuffer;

/**
 * @author KenM
 * @param <T>
 */
public abstract class AbstractPacket<T extends MMOClient> implements NetworkPacket {

	protected ByteBuffer _buf;

	T _client;

	@Override
	public final T getClient() {
		return _client;
	}
}
