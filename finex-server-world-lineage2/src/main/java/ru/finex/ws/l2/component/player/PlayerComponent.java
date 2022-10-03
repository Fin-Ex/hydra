package ru.finex.ws.l2.component.player;

import lombok.Getter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.model.entity.PlayerComponentEntity;
import ru.finex.ws.l2.persistence.PlayerComponentPersistenceService;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
public class PlayerComponent extends AbstractComponent {

    @Getter
    @PersistenceField(PlayerComponentPersistenceService.class)
    private PlayerComponentEntity entity = new PlayerComponentEntity();

    public void setLogin(String login) {
        if (entity.getLogin() != null) {
            return;
        }

        entity.setLogin(login);
    }

}
