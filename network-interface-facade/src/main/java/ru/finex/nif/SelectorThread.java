package ru.finex.nif;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author m0nster.mind
 */
public interface SelectorThread {

    void openServerSocket(InetAddress address, int tcpPort) throws IOException;
    void start();
    void shutdown();

}
