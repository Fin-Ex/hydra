package ru.finex.ws.l2.component.player;

import lombok.Getter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.model.entity.ActiveClassComponentEntity;
import ru.finex.ws.l2.persistence.ClassComponentPersistenceService;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
public class ClassComponent extends AbstractComponent {

    @Getter
    @PersistenceField(ClassComponentPersistenceService.class)
    private ActiveClassComponentEntity entity = new ActiveClassComponentEntity();

}
