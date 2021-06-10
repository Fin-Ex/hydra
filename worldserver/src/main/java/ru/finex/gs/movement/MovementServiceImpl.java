package ru.finex.gs.movement;

import ru.finex.gs.model.GameObject;
import ru.finex.gs.model.component.base.CoordinateComponent;
import ru.finex.gs.service.MovementService;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class MovementServiceImpl implements MovementService {

    @Override
    public void moveTo(GameObject gameObject, double x, double y, double z) {
        CoordinateComponent coordinateComponent = gameObject.getComponent(CoordinateComponent.class);
        coordinateComponent.setXYZ(x, y, z);
    }

}
