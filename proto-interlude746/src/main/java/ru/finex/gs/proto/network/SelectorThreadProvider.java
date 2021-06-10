package ru.finex.gs.proto.network;

import lombok.extern.slf4j.Slf4j;
import ru.finex.nif.SelectorThread;
import sf.l2j.commons.mmocore.IPacketHandler;
import sf.l2j.commons.mmocore.SelectorConfig;
import sf.l2j.commons.mmocore.SelectorThreadImpl;

import java.io.IOException;
import java.io.UncheckedIOException;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class SelectorThreadProvider implements Provider<SelectorThread> {

    private final SelectorThreadImpl<L2GameClient> selectorThread;

    public SelectorThreadProvider(
        Provider<NetworkClientFactory> clientFactory,
        Provider<PacketExecutor> packetExecutor,
        Provider<IPacketHandler> packetHandler) {
        final SelectorConfig sc = new SelectorConfig();
        sc.MAX_READ_PER_PASS = 80;
        sc.MAX_SEND_PER_PASS = 80;
        sc.SLEEP_TIME = 20;
        sc.HELPER_BUFFER_COUNT = 20;

        try {
            selectorThread = new SelectorThreadImpl<>(sc, packetExecutor.get(), packetHandler.get(), clientFactory.get(), null);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        try {
            selectorThread.openServerSocket(null, 7777);
        } catch (IOException e) {
            log.error("FATAL: Failed to open server socket.", e);
            System.exit(1);
        }
    }

    @Override
    public SelectorThread get() {
        return selectorThread;
    }
}
