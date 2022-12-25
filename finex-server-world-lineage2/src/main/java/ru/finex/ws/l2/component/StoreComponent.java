package ru.finex.ws.l2.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.finex.ws.l2.model.StoreType;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StoreComponent extends AbstractComponent {

    private StoreType storeType = StoreType.NONE;

}
