package sf.finex.model.component.player;

import lombok.extern.slf4j.Slf4j;
import sf.finex.model.GameObject;
import sf.finex.model.component.AbstractComponent;
import sf.l2j.commons.mmocore.ReceivablePacket;
import sf.l2j.gameserver.network.L2GameClient;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author m0nster.mind
 */
@Slf4j
public class NetworkComponent extends AbstractComponent {

    private final Queue<ReceivablePacket<L2GameClient>> incomeActions = new ConcurrentLinkedQueue<>();

    @Override
    public void onUpdate() {
        GameObject gameObject = getGameObject();

        for (int i = 0; i < 10; i++) {
            var packet = incomeActions.poll();
            if (packet == null) {
                break;
            }

            gameObject.getInjector().injectMembers(packet);

            try {
                packet.run();
            } catch (Exception e) {
                log.error("Fail to process packet {} for GameObject '{}'", packet.getClass().getCanonicalName(), gameObject, e);
            }
        }
    }

    public void addAction(ReceivablePacket<L2GameClient> action) {
        incomeActions.add(action);
    }

    @Override
    public int getExecutePriority() {
        return ORDER_PRIORITY_FIRST;
    }
}
