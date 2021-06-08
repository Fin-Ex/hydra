package sf.finex.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sf.finex.model.player.PlayerAppearanceClass;
import sf.finex.model.player.PvpMode;
import sf.l2j.gameserver.model.base.ClassRace;
import sf.l2j.gameserver.model.base.Sex;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity implements Entity {

    private int persistenceId;

    private ClassRace race;
    private Sex sex;
    private PlayerAppearanceClass appearanceClass;
    private int hairType;
    private int hairColor;
    private int faceType;

    private PvpMode pvpMode;

    private String name;
    private int nameColor;

    private String title;
    private int titleColor;

}
