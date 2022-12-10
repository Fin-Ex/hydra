package ru.finex.ws.l2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Range;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.ws.l2.model.Gender;
import ru.finex.ws.l2.model.PlayerAppearanceClass;
import ru.finex.ws.l2.model.PvpMode;
import ru.finex.ws.l2.model.enums.Race;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "game_object_player_components")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "game_object_player_components_id_seq",
    sequenceName = "game_object_player_components_id_seq", allocationSize = 1)
public class PlayerComponentEntity implements EntityObject<Integer>, GameObjectRelation {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "game_object_player_components_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @Column(name = "game_object_id", unique = true, nullable = false)
    private Integer gameObjectPersistenceId;

    @NotNull
    private String login;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Race race;
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private PlayerAppearanceClass appearanceClass;
    @Range(min = 0, max = 6)
    @NotNull
    private Integer hairType;
    @Range(min = 0, max = 3)
    @NotNull
    private Integer hairColor;
    @Range(min = 0, max = 2)
    @NotNull
    private Integer faceType;
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private PvpMode pvpMode;
    @NotNull
    @Max(16)
    private String name;
    @NotNull
    @ColumnDefault("-1")
    private Integer nameColor;
    @NotNull
    @Max(16)
    private String title;
    @NotNull
    @ColumnDefault("-1")
    private Integer titleColor;

}
