package ru.finex.ws.l2.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import ru.finex.ws.l2.network.GameCrypt;

import java.util.List;

/**
 * @author m0nster.mind
 */
public class PayloadCodec extends ByteToMessageCodec<ByteBuf> {

    private final GameCrypt crypt = new GameCrypt();

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        msg.resetReaderIndex();
        crypt.encrypt(msg);
        msg.resetReaderIndex();
        out.writeBytes(msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.resetReaderIndex();
        crypt.decrypt(in);
        in.readerIndex(in.writerIndex());
        out.add(in.retainedSlice(0, in.readerIndex()));
    }
}
