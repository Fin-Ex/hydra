package ru.finex.auth.hydra.network.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import ru.finex.core.network.PacketMetadata;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.PacketDeserializer;

import java.net.SocketAddress;
import java.util.List;

/**
 * @author m0nster.mind
 */
@Slf4j
public class LogDecoder extends MessageToMessageDecoder<Pair<PacketMetadata<PacketDeserializer<?>>, NetworkDto>> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Pair<PacketMetadata<PacketDeserializer<?>>, NetworkDto> msg, List<Object> out) throws Exception {
        if (log.isDebugEnabled()) {
            String clientAddress;
            SocketAddress address = ctx.channel().remoteAddress();
            if (address == null) {
                clientAddress = "[disconnected]";
            } else {
                clientAddress = address.toString();
            }

            log.debug("{} -> {}", clientAddress, msg.getRight());
        }

        out.add(msg);
    }

}
