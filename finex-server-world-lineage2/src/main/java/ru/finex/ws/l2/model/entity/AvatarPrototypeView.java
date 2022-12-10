package ru.finex.ws.l2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import ru.finex.core.model.entity.Projection;
import ru.finex.ws.l2.model.enums.Gender;
import ru.finex.ws.l2.model.PlayerAppearanceClass;
import ru.finex.ws.l2.model.enums.Race;

import java.util.Optional;

/**
 * @author m0nster.mind
 */
@Projection
public interface AvatarPrototypeView {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Race getRace();

    @Enumerated(EnumType.STRING)
    Gender getGender();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PlayerAppearanceClass getAppearanceClass();

    @Column(name = "str", nullable = false)
    Integer getSTR();

    @Column(name = "dex", nullable = false)
    Integer getDEX();

    @Column(name = "con", nullable = false)
    Integer getCON();

    @Column(name = "int", nullable = false)
    Integer getINT();

    @Column(name = "wit", nullable = false)
    Integer getWIT();

    @Column(name = "men", nullable = false)
    Integer getMEN();

    @Column(name = "luc", nullable = false)
    Integer getLUC();

    @Column(name = "cha", nullable = false)
    Integer getCHA();

    default int getClassId() {
        Race race = Optional.ofNullable(getRace())
            .orElse(Race.HUMAN);

        Gender gender = Optional.ofNullable(getGender())
            .orElse(Gender.MALE);

        return getAppearanceClass().getNetworkId(race, gender);
    }

}
