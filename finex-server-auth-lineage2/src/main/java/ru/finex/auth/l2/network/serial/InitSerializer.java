package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.l2.network.model.dto.InitDto;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(value = @Opcode(0x00))
public class InitSerializer implements PacketSerializer<InitDto> {

    @Override
    public void serialize(InitDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getSessionId());
        buffer.writeIntLE(dto.getProtocolRevision());
        buffer.writeBytes(scrambleModulus(((RSAPublicKey) dto.getPublicKey()).getPublicExponent()));
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeBytes(dto.getBlowfishKey().getEncoded());
        buffer.writeByte(0x00); // null-term of blowfish key
    }

    private byte[] scrambleModulus(BigInteger modulus) {
        byte[] scrambledMod = modulus.toByteArray();

        if ((scrambledMod.length == 0x81) && (scrambledMod[0] == 0x00)) {
            final byte[] temp = new byte[0x80];
            System.arraycopy(scrambledMod, 1, temp, 0, 0x80);
            scrambledMod = temp;
        }
        // step 1 : 0x4d-0x50 <-> 0x00-0x04
        for (int i = 0; i < 4; i++) {
            final byte temp = scrambledMod[0x00 + i];
            scrambledMod[0x00 + i] = scrambledMod[0x4d + i];
            scrambledMod[0x4d + i] = temp;
        }
        // step 2 : xor first 0x40 bytes with last 0x40 bytes
        for (int i = 0; i < 0x40; i++) {
            scrambledMod[i] = (byte) (scrambledMod[i] ^ scrambledMod[0x40 + i]);
        }
        // step 3 : xor bytes 0x0d-0x10 with bytes 0x34-0x38
        for (int i = 0; i < 4; i++) {
            scrambledMod[0x0d + i] = (byte) (scrambledMod[0x0d + i] ^ scrambledMod[0x34 + i]);
        }
        // step 4 : xor last 0x40 bytes with first 0x40 bytes
        for (int i = 0; i < 0x40; i++) {
            scrambledMod[0x40 + i] = (byte) (scrambledMod[0x40 + i] ^ scrambledMod[i]);
        }

        return scrambledMod;
    }

}
