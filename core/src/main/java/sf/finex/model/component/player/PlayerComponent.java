package sf.finex.model.component.player;

import lombok.Data;
import sf.finex.model.component.AbstractComponent;
import sf.finex.model.entity.PlayerEntity;
import sf.finex.persistence.PersistenceField;
import sf.finex.persistence.component.PlayerPersistenceService;

/**
 * @author m0nster.mind
 */
@Data
public class PlayerComponent extends AbstractComponent {

    @PersistenceField(PlayerPersistenceService.class)
    private PlayerEntity entity;

}
