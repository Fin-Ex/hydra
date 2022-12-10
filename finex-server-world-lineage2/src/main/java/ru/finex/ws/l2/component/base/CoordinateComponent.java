package ru.finex.ws.l2.component.base;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.model.entity.PositionComponentEntity;
import ru.finex.ws.l2.persistence.PositionComponentPersistence;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * Базовый компонент, хранящий в себе координаты игрового объекта на карте мира.
 *
 * @author m0nster.mind
 */
public class CoordinateComponent extends AbstractComponent {

    @Getter
    @PersistenceField(PositionComponentPersistence.class)
    private PositionComponentEntity entity = new PositionComponentEntity();

    @Getter
    @Setter
    private Vector3f destination;

    @Getter
    @Setter
    private Vector3f startMovementPosition;

    public Vector3f getPositionAsVector3f() {
        return new Vector3f(
            entity.getX().floatValue(),
            entity.getY().floatValue(),
            entity.getZ().floatValue()
        );
    }

    public void setPosition(double x, double y, double z) {
        entity.setXYZ(x, y, z);
    }

    public void setPosition(Vector3f position) {
        entity.setXYZ(position.getX(), position.getY(), position.getZ());
    }
}
