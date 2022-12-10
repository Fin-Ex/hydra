package ru.finex.ws.l2.service.component;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.Component;
import ru.finex.core.component.ComponentService;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.object.GameObject;
import ru.finex.ws.l2.component.base.CoordinateComponent;
import ru.finex.ws.l2.component.player.ClientComponent;
import ru.finex.ws.l2.component.player.SpeedComponent;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.tick.RegisterTick;
import ru.finex.ws.tick.TickService;
import ru.finex.ws.tick.TickStage;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CoordinateService {

    private final ComponentService componentService;
    private final TickService tickService;
    private final OutcomePacketBuilderService packets;

    /**
     * Begin object movement.
     * @param gameObject game object
     * @param destination destination position
     */
    public void move(GameObject gameObject, Vector3f destination) {
        CoordinateComponent component = componentService.getComponent(gameObject, CoordinateComponent.class);
        component.setStartMovementPosition(null);
        component.setDestination(destination);
    }

    public void setPosition(GameObject gameObject, Vector3f position) {
        CoordinateComponent component = componentService.getComponent(gameObject, CoordinateComponent.class);
        component.setPosition(position);
    }

    @RegisterTick(TickStage.PHYSICS)
    public void onPhysicsUpdate() {
        List<Component> components = componentService.getComponents(CoordinateComponent.class);
        for (int i = 0; i < components.size(); i++) {
            onPhysicsUpdateComponent((CoordinateComponent) components.get(i));
        }
    }

    private void onPhysicsUpdateComponent(CoordinateComponent component) {
        Vector3f target = component.getDestination();
        if (target == null) {
            return;
        }

        Vector3f position = component.getPositionAsVector3f();
        if (component.getStartMovementPosition() == null) {
            component.setStartMovementPosition(position);
            notifyClients(component.getGameObject());
        }

        SpeedComponent speed = componentService.getComponent(component.getGameObject(), SpeedComponent.class);

        float distance = (float)speed.getRunSpeed() * tickService.getDeltaTime();
        position.moveToPoint(target, distance);
        component.setPosition(position);

        if (position.equals(target, 0.01f)) {
            component.setStartMovementPosition(null);
            component.setDestination(null);
        }
    }

    private void notifyClients(GameObject initiator) {
        ClientComponent clientComponent = componentService.getComponent(initiator, ClientComponent.class);
        GameClient session = clientComponent.getClient();
        session.sendPacket(packets.moveToLocation(initiator));
    }

}
