package ru.finex.ws.l2.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "game_object_clan_components")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "game_object_clan_components_id_seq",
    sequenceName = "game_object_clan_components_id_seq", allocationSize = 1)
public class ClanComponentEntity implements EntityObject<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "game_object_clan_components_id_seq", strategy = GenerationType.SEQUENCE)
    private Integer persistenceId;

    @Column(name = "game_object_id", unique = true, nullable = false)
    private Integer gameObjectPersistenceId;

    @JoinColumn(name = "clan_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ClanEntity clan;

    public int getClanId() {
        if (clan == null) {
            return 0;
        }

        return clan.getPersistenceId();
    }

}
