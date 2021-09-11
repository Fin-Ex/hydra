package ru.finex.ws.l2.component.player;

import lombok.Getter;
import lombok.Setter;
import ru.finex.ws.l2.model.StoreType;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
public class StoreComponent extends AbstractComponent {

    @Getter @Setter
    private StoreType storeType = StoreType.NONE;

}
