package ru.finex.ws.l2.movement;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.GameObject;
import ru.finex.ws.l2.component.base.CoordinateComponent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class MovementServiceImpl implements MovementService {

    private final ComponentService componentService;

    @Override
    public void moveTo(GameObject gameObject, double x, double y, double z) {
        CoordinateComponent coordinateComponent = componentService.getComponent(gameObject, CoordinateComponent.class);
        coordinateComponent.setXYZ(x, y, z);
    }

}
