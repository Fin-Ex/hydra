package ru.finex.gs.network;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.ServerContext;
import sf.l2j.commons.mmocore.ReceivablePacket;
import sf.l2j.gameserver.network.L2GameClient;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ServicePacketExecutor {

    private final Queue<ReceivablePacket<L2GameClient>> queue = new ConcurrentLinkedQueue<>();
    private final ServerContext context;

    public void scheduleExecutePacket(ReceivablePacket<L2GameClient> packet) {
        queue.offer(packet);
    }

    public void executeQueue() {
        for (int i = 0; i < 10; i++) {
            var packet = queue.poll();
            if (packet == null) {
                break;
            }

            context.getInjector().injectMembers(packet);
            try {
                packet.run();
            } catch (Exception e) {
                log.error("Fail to process packet: {} for {} client", packet.getClass().getCanonicalName(), packet.getClient(), e);
            }
        }
    }

}
