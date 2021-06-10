package ru.finex.gs.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.Entity;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionEntity implements Entity {

    private int persistenceId; // ID компонента, в бд: id
    private int gameObjectPersistenceId; // ID игрового объекта в БД, к которому относится компонент, в базе: go_id
    private double x;
    private double y;
    private double z;
    private double h;

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public PositionEntity clone() {
        try {
            return (PositionEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
