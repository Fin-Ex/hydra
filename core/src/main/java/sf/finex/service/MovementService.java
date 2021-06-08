package sf.finex.service;

import com.google.inject.ImplementedBy;
import sf.finex.model.GameObject;
import sf.finex.movement.MovementServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(MovementServiceImpl.class)
public interface MovementService {

    void moveTo(GameObject gameObject, double x, double y, double z);

}
