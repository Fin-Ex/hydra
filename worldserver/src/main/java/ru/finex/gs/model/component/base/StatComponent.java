package ru.finex.gs.model.component.base;

import lombok.Getter;
import ru.finex.gs.model.component.AbstractComponent;
import ru.finex.gs.model.entity.StatEntity;
import ru.finex.gs.persistence.PersistenceField;
import ru.finex.gs.persistence.component.PlayerPersistenceService;

/**
 * @author finfan
 */
public class StatComponent extends AbstractComponent {
	@Getter
	@PersistenceField(PlayerPersistenceService.class)
	private StatEntity entity;
}
