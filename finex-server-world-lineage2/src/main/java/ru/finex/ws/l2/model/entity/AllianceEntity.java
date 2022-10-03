package ru.finex.ws.l2.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "alliances")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "alliances_id_seq", sequenceName = "alliances_id_seq", allocationSize = 1)
public class AllianceEntity implements EntityObject<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "alliances_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @Column(unique = true, nullable = false, length = 24)
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "crest_id", referencedColumnName = "id")
    private AllianceCrest crest;

}
