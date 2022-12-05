package ru.finex.ws.l2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.entity.EntityObject;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "game_object_status_components")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "game_object_status_components_id_seq",
    sequenceName = "game_object_status_components_id_seq", allocationSize = 1)
public class StatusComponentEntity implements EntityObject<Integer>, GameObjectRelation {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "game_object_status_components_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @Column(name = "game_object_id", unique = true, nullable = false)
    private Integer gameObjectPersistenceId;

    @NotNull
    private Double hp;
    @NotNull
    private Double maxHp;
    @NotNull
    private Double mp;
    @NotNull
    private Double maxMp;
    @NotNull
    private Double cp;
    @NotNull
    private Double maxCp;

}
