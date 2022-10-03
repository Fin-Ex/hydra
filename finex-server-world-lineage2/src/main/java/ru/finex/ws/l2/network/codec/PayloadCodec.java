package ru.finex.ws.l2.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * @author m0nster.mind
 */
public class PayloadCodec extends ByteToMessageCodec<ByteBuf> {

    private final byte[] inKey = new byte[16];
    private final byte[] outKey = new byte[16];
    private boolean isEnabled;

    public void setKey(byte[] key) {
        System.arraycopy(key, 0, inKey, 0, 16);
        System.arraycopy(key, 0, outKey, 0, 16);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (!isEnabled) {
            out.writeBytes(msg);
            isEnabled = true;
            return;
        }

        for (int index = 0, encrypted = 0x00; index < msg.writerIndex(); index++) {
            int origin = msg.getUnsignedByte(index);
            encrypted = origin ^ outKey[index & 0x0f] ^ encrypted;
            out.writeByte(encrypted);
        }

        // Shift key.
        int old = outKey[8] & 0xff;
        old |= (outKey[9] << 8) & 0xff00;
        old |= (outKey[10] << 16) & 0xff0000;
        old |= (outKey[11] << 24) & 0xff000000;

        old += msg.writerIndex();

        outKey[8] = (byte) (old & 0xff);
        outKey[9] = (byte) ((old >> 8) & 0xff);
        outKey[10] = (byte) ((old >> 16) & 0xff);
        outKey[11] = (byte) ((old >> 24) & 0xff);

        msg.readerIndex(msg.writerIndex());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!isEnabled) {
            out.add(in.readerIndex(in.writerIndex())
                .retainedSlice(0, in.writerIndex())
            );
            return;
        }

        for (int index = 0, prevByte = 0x00; index < in.writerIndex(); index++) {
            int origin = in.getUnsignedByte(index);
            in.setByte(index, origin ^ inKey[index & 0x0f] ^ prevByte);
            prevByte = origin;
        }

        int old = inKey[8] & 0xff;
        old |= inKey[9] << 8 & 0xff00;
        old |= inKey[10] << 0x10 & 0xff0000;
        old |= inKey[11] << 0x18 & 0xff000000;

        old += in.writerIndex();

        inKey[8] = (byte) (old & 0xff);
        inKey[9] = (byte) (old >> 0x08 & 0xff);
        inKey[10] = (byte) (old >> 0x10 & 0xff);
        inKey[11] = (byte) (old >> 0x18 & 0xff);

        out.add(in.readerIndex(in.writerIndex())
            .retainedSlice(0, in.writerIndex())
        );
    }
}
