package ru.finex.gs.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.Entity;
import ru.finex.gs.model.Gender;
import ru.finex.gs.model.PlayerAppearanceClass;
import ru.finex.gs.model.PlayerRace;
import ru.finex.gs.model.PvpMode;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity implements Entity {

    private int persistenceId;

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
