package ru.finex.gs.model.component.player;

import lombok.Data;
import ru.finex.gs.model.component.AbstractComponent;
import ru.finex.gs.model.entity.ClanEntity;
import ru.finex.gs.persistence.PersistenceField;
import ru.finex.gs.persistence.component.ClanPersistenceService;

/**
 * @author m0nster.mind
 */
@Data
public class ClanComponent extends AbstractComponent {

    @PersistenceField(ClanPersistenceService.class)
    private ClanEntity entity;

}
