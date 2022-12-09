package ru.finex.ws.l2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.ws.l2.model.Gender;
import ru.finex.ws.l2.model.PlayerAppearanceClass;
import ru.finex.ws.l2.model.enums.Race;

import java.time.Instant;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "game_object_avatars")
@Immutable
@NoArgsConstructor
@AllArgsConstructor
public class AvatarView implements EntityObject<Integer> {

    @Id
    @Column(name = "id")
    private Integer persistenceId;

    private String name;
    private String login;

    private Integer clanId;

    private transient Integer builderLevel = 0;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    @Enumerated(EnumType.ORDINAL)
    private Race race;
    @Enumerated(EnumType.ORDINAL)
    private PlayerAppearanceClass appearanceClass;

    private Double x;
    private Double y;
    private Double z;

    private Double hp;
    private Double mp;

    private transient Long sp = 0L;
    private transient Long exp = 0L;
    private transient Double expPercent = 0.;

    private transient Integer level = 1;
    private transient Integer reputation = 0;
    private transient Integer pkCount = 0;
    private transient Integer pvpCount = 0;

    private Integer hairType;
    private Integer hairColor;
    private Integer faceType;

    private Double maxHp;
    private Double maxMp;

    private Instant deleteDate;

    private transient Integer petPrototypeId = 0;
    private transient Integer petLevel = 0;
    private transient Integer petFood = 0;
    private transient Integer petFoodLevel = 0;
    private transient Double petHp = 0.;
    private transient Double petMp = 0.;

    private transient Integer vitalityCount = 0;
    private transient Integer vitalityPercent = 0;
    private transient Integer vitalityItemsUsed = 0;

    private transient Boolean isAccessible = true;

    private transient Boolean isNoble = false;
    private transient Boolean isHero = false;
    private transient Boolean isShowHairAccessory = false;

    public int getClassId() {
        return appearanceClass.getNetworkId(race, gender);
    }

}
