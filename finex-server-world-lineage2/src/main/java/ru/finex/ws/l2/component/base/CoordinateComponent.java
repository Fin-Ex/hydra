package ru.finex.ws.l2.component.base;

import lombok.Getter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.gs.model.component.AbstractComponent;
import ru.finex.ws.l2.model.entity.PositionEntity;
import ru.finex.ws.l2.persistence.PositionPersistence;

/**
 * Базовый компонент, хранящий в себе координаты игрового объекта на карте мира.
 *
 * @author m0nster.mind
 */
public class CoordinateComponent extends AbstractComponent {

    @Getter
    @PersistenceField(PositionPersistence.class)
    private PositionEntity position;

    public void setXYZ(double x, double y, double z) {
        position.setXYZ(x, y, z);
    }

}
