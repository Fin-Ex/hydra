package ru.finex.ws.l2.component.base;

import lombok.Getter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.model.entity.StatComponentEntity;
import ru.finex.ws.l2.persistence.PlayerComponentPersistenceService;
import ru.finex.ws.l2.persistence.StatComponentPersistenceService;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author finfan
 */
public class StatComponent extends AbstractComponent {
	@Getter
	@PersistenceField(StatComponentPersistenceService.class)
	private StatComponentEntity entity = new StatComponentEntity();
}
