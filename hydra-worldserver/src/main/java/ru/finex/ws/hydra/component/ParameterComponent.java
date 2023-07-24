package ru.finex.ws.hydra.component;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.hydra.model.entity.ParameterComponentEntity;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author finfan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ParameterComponent extends AbstractComponent {

	@Setter(AccessLevel.NONE)
	@PersistenceField
	private ParameterComponentEntity entity = new ParameterComponentEntity();

}
