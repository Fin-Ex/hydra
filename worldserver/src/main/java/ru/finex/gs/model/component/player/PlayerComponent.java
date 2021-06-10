package ru.finex.gs.model.component.player;

import lombok.Data;
import ru.finex.gs.model.component.AbstractComponent;
import ru.finex.gs.model.entity.PlayerEntity;
import ru.finex.gs.persistence.PersistenceField;
import ru.finex.gs.persistence.component.PlayerPersistenceService;

/**
 * @author m0nster.mind
 */
@Data
public class PlayerComponent extends AbstractComponent {

    @PersistenceField(PlayerPersistenceService.class)
    private PlayerEntity entity;

}
