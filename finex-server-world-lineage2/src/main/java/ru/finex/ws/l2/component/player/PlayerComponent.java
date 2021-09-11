package ru.finex.ws.l2.component.player;

import lombok.Getter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.ws.l2.persistence.PlayerPersistenceService;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
public class PlayerComponent extends AbstractComponent {

    @Getter
    @PersistenceField(PlayerPersistenceService.class)
    private PlayerEntity entity;

}
