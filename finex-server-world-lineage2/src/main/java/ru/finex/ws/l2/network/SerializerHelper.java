package ru.finex.ws.l2.network;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class SerializerHelper {

    public static String readStringNullTerm(ByteBuf buffer) {
        StringBuilder sb = new StringBuilder();
        for (char symbol = Character.reverseBytes(buffer.readChar()); symbol != 0; symbol = Character.reverseBytes(buffer.readChar())) {
            sb.append(symbol);
        }
        return sb.toString();
    }

    public static String readString(ByteBuf buffer) {
        int length = buffer.readUnsignedShortLE();
        byte[] payload = new byte[length];
        buffer.readBytes(payload);
        return new String(payload, StandardCharsets.UTF_8);
    }

    public static void writeStringNullTerm(ByteBuf buffer, String value) {
        for (int i = 0; i < value.length(); i++) {
            buffer.writeChar(Character.reverseBytes(value.charAt(i)));
        }

        buffer.writeChar(0x00);
    }

    public static void writeString(ByteBuf buffer, String value) {
        if(value != null) {
            buffer.writeShortLE(value.length());
            for (int i = 0; i < value.length(); i++) {
                buffer.writeChar(Character.reverseBytes(value.charAt(i)));
            }
        } else {
            buffer.writeShort(0x00);
        }
    }

}
