package ru.finex.auth.l2.network.codec;

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


            while (msg.readableBytes() > 0) {
                for (int column = 0; column < 5; column++) {
                    for (int i = 0; i < 4; i++) {
                        if (msg.readableBytes() == 0) {
                            hexLine.append("__");
                        } else {
                            String hex = Integer.toHexString(msg.readByte() & 0xff);
                            if (hex.length() == 1) {
                                hexLine.append('0');
                            }
                            hexLine.append(hex);
                        }
                        hexLine.append(' ');
                    }
                    hexLine.append("  ");
                }
                hexLine.append("\n");
            }

            log.debug(hexLine.toString());
            msg.resetReaderIndex();
        }


        out.add(msg.retain());
    }

}
