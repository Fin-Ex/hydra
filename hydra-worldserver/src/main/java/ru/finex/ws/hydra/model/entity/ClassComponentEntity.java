package ru.finex.ws.hydra.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.ws.hydra.model.enums.ClassId;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "game_object_class_components")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "is_active", discriminatorType = DiscriminatorType.INTEGER)
@SequenceGenerator(name = "game_object_class_components_id_seq",
    sequenceName = "game_object_class_components_id_seq", allocationSize = 1)
public class ClassComponentEntity implements EntityObject<Integer>, GameObjectRelation {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "game_object_class_components_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @Column(name = "game_object_id", unique = true, nullable = false)
    private Integer gameObjectPersistenceId;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private ClassId classId;

    @NotNull
    @Min(0)
    private Integer level;

    @NotNull
    @Min(0)
    private Long exp;

    @NotNull
    @Min(0)
    private Long sp;

//    @NotNull
//    @ColumnDefault("true")
//    private Boolean isActive;
}
