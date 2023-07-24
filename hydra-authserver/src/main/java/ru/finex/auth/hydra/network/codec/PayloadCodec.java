package ru.finex.auth.hydra.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import ru.finex.auth.hydra.utils.AuthCrypt;

import java.util.List;
import javax.crypto.SecretKey;

/**
 * @author m0nster.mind
 */
public class PayloadCodec extends ByteToMessageCodec<ByteBuf> {

    private final AuthCrypt crypt;

    public PayloadCodec(SecretKey key) {
        crypt = new AuthCrypt(key);
    }

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
        out.add(in.retainedSlice(0, in.writerIndex()));
    }
}
