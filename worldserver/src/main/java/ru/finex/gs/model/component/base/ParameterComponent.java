package ru.finex.gs.model.component.base;

import lombok.Getter;
import ru.finex.gs.model.component.AbstractComponent;
import ru.finex.gs.model.entity.ParameterEntity;
import ru.finex.gs.persistence.PersistenceField;
import ru.finex.gs.persistence.component.PlayerPersistenceService;

/**
 * @author finfan
 */
public class ParameterComponent extends AbstractComponent {
	@Getter
	@PersistenceField(PlayerPersistenceService.class)
	private ParameterEntity entity;
}
