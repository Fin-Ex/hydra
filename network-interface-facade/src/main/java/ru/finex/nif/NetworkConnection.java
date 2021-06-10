package ru.finex.nif;

import java.net.InetAddress;

/**
 * @author m0nster.mind
 */
public interface NetworkConnection {

    NetworkClient getClient();
    InetAddress getInetAddress();
    int getPort();
    boolean isClosed();

    void sendPacket(OutcomePacket sp);
    void close(OutcomePacket sp);

}
