package ru.finex.gs.model.component.player;

import lombok.Data;
import ru.finex.gs.model.StoreType;
import ru.finex.gs.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class StoreComponent extends AbstractComponent {

    private StoreType storeType = StoreType.NONE;

}
