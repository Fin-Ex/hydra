package ru.finex.auth.l2.network.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import ru.finex.network.netty.model.NetworkDto;

import java.net.SocketAddress;
import java.util.List;

/**
 * @author m0nster.mind
 */
@Slf4j
public class LogEncoder extends MessageToMessageEncoder<NetworkDto> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NetworkDto msg, List<Object> out) throws Exception {
        if (log.isDebugEnabled()) {
            String clientAddress;
            SocketAddress address = ctx.channel().remoteAddress();
            if (address == null) {
                clientAddress = "[disconnected]";
            } else {
                clientAddress = address.toString();
            }

            log.debug("{} <- {}", clientAddress, msg);
        }

        out.add(msg);
    }

}
