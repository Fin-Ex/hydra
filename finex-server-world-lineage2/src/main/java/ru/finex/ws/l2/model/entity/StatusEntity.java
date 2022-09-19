package ru.finex.ws.l2.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.EntityObject;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusEntity implements EntityObject<Integer> {

    private Integer persistenceId;
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
