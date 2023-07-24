package ru.finex.ws.hydra.model.entity;

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
import ru.finex.core.model.entity.EntityObject;

/**
 * @author m0nster.mind
 */
@Data
@Entity
@Table(name = "alliance_crests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "crests_id_seq", sequenceName = "crests_id_seq", allocationSize = 1)
public class AllianceCrest implements EntityObject<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "crests_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    private byte[] crest;

}
