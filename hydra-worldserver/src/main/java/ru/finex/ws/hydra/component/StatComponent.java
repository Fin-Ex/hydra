package ru.finex.ws.hydra.component;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.hydra.model.entity.StatComponentEntity;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author finfan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatComponent extends AbstractComponent {

	@Setter(AccessLevel.NONE)
	@PersistenceField
	private StatComponentEntity entity = new StatComponentEntity();

}
