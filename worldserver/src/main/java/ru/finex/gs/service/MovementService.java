package ru.finex.gs.service;

import com.google.inject.ImplementedBy;
import ru.finex.gs.model.GameObject;
import ru.finex.gs.movement.MovementServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(MovementServiceImpl.class)
public interface MovementService {

    void moveTo(GameObject gameObject, double x, double y, double z);

}
