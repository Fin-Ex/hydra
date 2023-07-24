package ru.finex.ws.hydra.utils;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class HexUtils {

    public static void appendBuffer(StringBuilder sb, ByteBuf msg) {
        while (msg.readableBytes() > 0) {
            for (int column = 0; column < 5; column++) {
                for (int i = 0; i < 4; i++) {
                    if (msg.readableBytes() == 0) {
                        sb.append("__");
                    } else {
                        String hex = Integer.toHexString(msg.readByte() & 0xff);
                        if (hex.length() == 1) {
                            sb.append('0');
                        }
                        sb.append(hex);
                    }
                    sb.append(' ');
                }
                sb.append("  ");
            }
            sb.append("\n");
        }
    }

}
