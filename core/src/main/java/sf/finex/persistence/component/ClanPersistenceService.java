package sf.finex.persistence.component;

import sf.finex.model.GameObject;
import sf.finex.model.entity.ClanEntity;
import sf.finex.service.persistence.PersistenceService;

import java.util.Collections;

/**
 * @author m0nster.mind
 */
public class ClanPersistenceService implements PersistenceService<ClanEntity> {

    @Override
    public ClanEntity persist(ClanEntity entity) {
        return entity;
    }

    @Override
    public ClanEntity restore(GameObject gameObject) {
        return ClanEntity.builder()
            .persistenceId(0)
            .crestId(0)
            .crest(new byte[0])
            .largeCrestId(0)
            .largeCrest(new byte[0])
            .members(Collections.emptyList())
            .build();
    }

}
