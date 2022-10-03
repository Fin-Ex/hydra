package ru.finex.ws.l2.component.player;

import lombok.Getter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.model.entity.ClanComponentEntity;
import ru.finex.ws.l2.persistence.ClanComponentPersistenceService;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
public class ClanComponent extends AbstractComponent {

    @Getter
    @PersistenceField(ClanComponentPersistenceService.class)
    private ClanComponentEntity entity = new ClanComponentEntity();

}
