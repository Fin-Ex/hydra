package ru.finex.ws.hydra.network.codec;

import io.netty.buffer.ByteBuf;
import ru.finex.network.netty.serial.OpcodeCodec;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class OpcodeCodecImpl implements OpcodeCodec {

    @Override
    public void encode(int[] opcodes, ByteBuf buffer) {
        buffer.writeByte(opcodes[0]);
        for (int i = 1; i < opcodes.length; i++) {
            buffer.writeShortLE(opcodes[i]);
        }
    }

    @Override
    public int[] decode(ByteBuf buffer) {
        int opcode = buffer.readUnsignedByte();
        if (opcode == 0xd0) {
            int subOpcode = buffer.readUnsignedShortLE();
            return new int[] { opcode, subOpcode };
        }

        return new int[] { opcode };
    }

}
