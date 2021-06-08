package sf.finex.model.component.player;

import lombok.Data;
import sf.finex.enums.EStoreType;
import sf.finex.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class StoreComponent extends AbstractComponent {

    private EStoreType storeType = EStoreType.NONE;

}
