package ru.finex.gs.model.component.base;

import lombok.Getter;
import ru.finex.gs.model.component.AbstractComponent;
import ru.finex.gs.model.entity.StatusEntity;
import ru.finex.gs.persistence.PersistenceField;
import ru.finex.gs.persistence.component.StatusPersistenceService;

/**
 * @author m0nster.mind
 */
public class StatusComponent extends AbstractComponent {

    @Getter
    @PersistenceField(StatusPersistenceService.class)
    private StatusEntity statusEntity;

}
