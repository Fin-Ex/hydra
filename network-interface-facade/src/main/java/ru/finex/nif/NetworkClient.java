package ru.finex.nif;

import java.nio.ByteBuffer;

/**
 * @author m0nster.mind
 */
public interface NetworkClient {

    NetworkConnection getConnection();

    boolean decrypt(ByteBuffer buf, int size);
    boolean encrypt(ByteBuffer buf, int size);

}
