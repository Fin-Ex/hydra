package ru.finex.ws.hydra.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "clans")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "clans_id_seq", sequenceName = "clans_id_seq", allocationSize = 1)
public class ClanEntity implements EntityObject<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "clans_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @Column(unique = true, nullable = false, length = 24)
    private String name;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "alliance_id", referencedColumnName = "id")
    private AllianceEntity alliance;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "crest_id", referencedColumnName = "id")
    private ClanCrest crest;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "large_crest_id", referencedColumnName = "id")
    private ClanLargeCrest largeCrest;

    @Override
    public ClanEntity clone() {
        try {
            return (ClanEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
