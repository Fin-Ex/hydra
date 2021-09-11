package ru.finex.ws.l2.component.player;

import lombok.Getter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.model.entity.ClanEntity;
import ru.finex.ws.l2.persistence.ClanPersistenceService;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
public class ClanComponent extends AbstractComponent {

    @Getter
    @PersistenceField(ClanPersistenceService.class)
    private ClanEntity entity;

}
