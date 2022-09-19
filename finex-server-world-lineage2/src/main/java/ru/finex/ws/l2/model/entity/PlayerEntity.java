package ru.finex.ws.l2.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.ws.l2.model.Gender;
import ru.finex.ws.l2.model.PlayerAppearanceClass;
import ru.finex.ws.l2.model.PlayerRace;
import ru.finex.ws.l2.model.PvpMode;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity implements EntityObject<Integer> {

    private Integer persistenceId;

    private PlayerRace race;
    private Gender gender;
    private PlayerAppearanceClass appearanceClass;
    private int hairType;
    private int hairColor;
    private int faceType;

    private PvpMode pvpMode;

    private String name;
    private int nameColor;

    private String title;
    private int titleColor;

    @Override
    public PlayerEntity clone() {
        try {
            return (PlayerEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
