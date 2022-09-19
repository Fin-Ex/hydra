package ru.finex.ws.l2.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.EntityObject;

import java.util.List;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClanEntity implements EntityObject<Integer> {

    private Integer persistenceId;

    private int crestId;
    private byte[] crest;

    private int largeCrestId;
    private byte[] largeCrest;

    private List<PlayerEntity> members;

    @Override
    public ClanEntity clone() {
        try {
            return (ClanEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
