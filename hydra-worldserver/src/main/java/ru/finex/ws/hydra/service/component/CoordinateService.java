package ru.finex.ws.hydra.service.component;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.impl.AbstractComponentLogicService;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.object.GameObject;
import ru.finex.ws.hydra.component.ClientComponent;
import ru.finex.ws.hydra.component.CoordinateComponent;
import ru.finex.ws.hydra.component.SpeedComponent;
import ru.finex.ws.hydra.network.OutcomePacketBuilderService;
import ru.finex.ws.hydra.network.session.GameClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CoordinateService extends AbstractComponentLogicService<CoordinateComponent> {

    private final OutcomePacketBuilderService packets;

    /**
     * Begin object movement.
     * @param gameObject game object
     * @param destination destination position
     */
    public void move(GameObject gameObject, Vector3f destination) {
        CoordinateComponent component = getComponent(gameObject);
        component.setStartMovementPosition(null);
        component.setDestination(destination);
    }

    public void setPosition(GameObject gameObject, Vector3f position) {
        CoordinateComponent component = getComponent(gameObject);
        component.setPosition(position);
    }

    @Override
    protected void onPhysics(CoordinateComponent component) {
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
