package ru.finex.ws.l2.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import ru.finex.ws.l2.model.Gender;
import ru.finex.ws.l2.model.PlayerAppearanceClass;
import ru.finex.ws.l2.model.enums.Race;

import java.util.Optional;

/**
 * Note: real mapping is declared in l2_Avatar.xml
 * @author m0nster.mind
 */
@Data
@Entity
public class AvatarPrototypeView {

    private transient String name;

    private String race;
    private String gender;
    private String appearanceClass;

    private int STR;
    private int DEX;
    private int CON;
    private int INT;
    private int WIT;
    private int MEN;
    private int LUC;
    private int CHA;

    public AvatarPrototypeView(String race, String gender, String appearanceClass, Integer STR, Integer DEX, Integer CON, Integer INT,
        Integer WIT, Integer MEN, Integer LUC, Integer CHA) {
        this.race = race;
        this.gender = gender;
        this.appearanceClass = appearanceClass;
        this.STR = STR;
        this.DEX = DEX;
        this.CON = CON;
        this.INT = INT;
        this.WIT = WIT;
        this.MEN = MEN;
        this.LUC = LUC;
        this.CHA = CHA;
    }

    public Race getRace() {
        return Optional.ofNullable(race)
            .map(Race::valueOf)
            .orElse(null);
    }

    public Gender getGender() {
        return Optional.ofNullable(gender)
            .map(Gender::valueOf)
            .orElse(null);
    }

    public PlayerAppearanceClass getAppearanceClass() {
        return PlayerAppearanceClass.valueOf(appearanceClass);
    }

    public int getClassId() {
        Race race = Optional.ofNullable(getRace())
            .orElse(Race.HUMAN);

        Gender gender = Optional.ofNullable(getGender())
            .orElse(Gender.MALE);

        return getAppearanceClass().getNetworkId(race, gender);
    }

}
