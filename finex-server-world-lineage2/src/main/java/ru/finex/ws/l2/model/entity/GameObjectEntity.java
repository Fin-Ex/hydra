package ru.finex.ws.l2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.finex.core.model.entity.EntityObject;

import java.time.Instant;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "game_objects")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "game_objects_id_seq", sequenceName = "game_objects_id_seq", allocationSize = 1)
public class GameObjectEntity implements EntityObject<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "game_objects_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @CreationTimestamp
    private Instant createDate;

    @CreationTimestamp
    @UpdateTimestamp
    private Instant updateTime;

    private Instant deleteDate;

}
