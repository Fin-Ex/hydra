package sf.l2j.commons.mmocore;

import java.nio.ByteBuffer;

/**
 * @author KenM
 * @param <T>
 */
public interface IPacketHandler<T extends MMOClient> {

	ReceivablePacket<T> handlePacket(ByteBuffer buf, T client);

}
