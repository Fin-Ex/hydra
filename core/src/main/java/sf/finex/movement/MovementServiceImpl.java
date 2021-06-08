package sf.finex.movement;

import sf.finex.model.GameObject;
import sf.finex.model.component.base.CoordinateComponent;
import sf.finex.service.MovementService;

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
