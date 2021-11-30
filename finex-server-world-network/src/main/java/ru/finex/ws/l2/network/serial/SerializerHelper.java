package ru.finex.ws.l2.network.serial;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

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

}
