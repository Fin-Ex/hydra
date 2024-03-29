package ru.finex.ws.hydra.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.finex.ws.hydra.model.MountType;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MountComponent extends AbstractComponent {

    private int mountId;
    private MountType mountType = MountType.NONE;

}
