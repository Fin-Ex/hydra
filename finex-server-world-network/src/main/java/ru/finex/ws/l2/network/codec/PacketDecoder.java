package ru.finex.ws.l2.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import ru.finex.ws.l2.network.PacketService;
import ru.finex.ws.l2.network.model.NetworkDto;
import ru.finex.ws.l2.network.serial.PacketSerializer;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Sharable
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PacketDecoder extends ByteToMessageDecoder {

    private final PacketService packetService;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        PacketSerializer<?> serializer = packetService.handlePacket(in);
        if (serializer != null) {
            NetworkDto dto = serializer.serialize(in);
            out.add(dto);
        }
    }

}
