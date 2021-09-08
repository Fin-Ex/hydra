package ru.finex.ws.l2.movement;

import com.google.inject.ImplementedBy;
import ru.finex.core.model.GameObject;

/**
 * @author m0nster.mind
 */
@ImplementedBy(MovementServiceImpl.class)
public interface MovementService {

    void moveTo(GameObject gameObject, double x, double y, double z);

}
