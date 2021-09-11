package ru.finex.ws.l2.component.base;

import lombok.Getter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.model.entity.StatusEntity;
import ru.finex.ws.l2.persistence.StatusPersistenceService;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
public class StatusComponent extends AbstractComponent {

    @Getter
    @PersistenceField(StatusPersistenceService.class)
    private StatusEntity statusEntity;

}
