package ru.finex.auth.hydra.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.List;

/**
 * @author m0nster.mind
 */
@Slf4j
public class LogLengthDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (log.isDebugEnabled()) {
            SocketAddress address = ctx.channel().remoteAddress();
            String clientAddress;
            if (address == null) {
                clientAddress = "[disconnected]";
            } else {
                clientAddress = address.toString();
            }

            log.debug("{} -> {} length in header", clientAddress, msg.getShortLE(0) & 0xffff);
        }

        out.add(msg.retain());
    }

}
