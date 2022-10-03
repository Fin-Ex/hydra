package ru.finex.ws.l2.component.base;

import lombok.Getter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.l2.model.entity.ParameterComponentEntity;
import ru.finex.ws.l2.persistence.ParameterComponentPersistenceService;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author finfan
 */
public class ParameterComponent extends AbstractComponent {
	@Getter
	@PersistenceField(ParameterComponentPersistenceService.class)
	private ParameterComponentEntity entity = new ParameterComponentEntity();
}
