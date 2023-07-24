package ru.finex.ws.hydra.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import ru.finex.ws.hydra.utils.HexUtils;

import java.net.SocketAddress;
import java.util.List;

/**
 * @author m0nster.mind
 */
@Slf4j
public class LogHexDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (log.isDebugEnabled()) {
            SocketAddress address = ctx.channel().remoteAddress();

            StringBuilder hexLine = new StringBuilder();
            if (address == null) {
                hexLine.append("[disconnected]");
            } else {
                hexLine.append(address);
            }
            hexLine.append(" -> Payload ").append(msg.readableBytes()).append(" bytes\n");
            HexUtils.appendBuffer(hexLine, msg);
            log.debug(hexLine.toString());

            msg.resetReaderIndex();
        }


        out.add(msg.retain());
    }

}
