package ru.finex.ws.l2.model.entity;

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
public class StatusEntity implements Entity {

    private int persistenceId;
    private double hp;
    private double maxHp;
    private double mp;
    private double maxMp;
    private double cp;
    private double maxCp;

    @Override
    public StatusEntity clone() {
        try {
            return (StatusEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
