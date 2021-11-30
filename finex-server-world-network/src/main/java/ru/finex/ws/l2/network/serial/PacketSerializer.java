package ru.finex.ws.l2.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.network.model.NetworkDto;

/**
 * @author m0nster.mind
 */
public interface PacketSerializer<T extends NetworkDto> {

    T serialize(ByteBuf buffer);

}
