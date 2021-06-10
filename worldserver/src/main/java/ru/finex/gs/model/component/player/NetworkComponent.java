package ru.finex.gs.model.component.player;

import lombok.extern.slf4j.Slf4j;
import ru.finex.gs.model.GameObject;
import ru.finex.gs.model.component.AbstractComponent;
import ru.finex.nif.IncomePacket;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author m0nster.mind
 */
@Slf4j
public class NetworkComponent extends AbstractComponent {

    private final Queue<IncomePacket> incomeActions = new ConcurrentLinkedQueue<>();

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

    public void addAction(IncomePacket action) {
        incomeActions.add(action);
    }

    @Override
    public int getExecutePriority() {
        return ORDER_PRIORITY_FIRST;
    }
}
