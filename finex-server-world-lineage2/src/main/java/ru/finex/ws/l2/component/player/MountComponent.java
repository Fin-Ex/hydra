package ru.finex.ws.l2.component.player;

import lombok.Data;
import ru.finex.ws.l2.model.MountType;
import ru.finex.gs.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class MountComponent extends AbstractComponent {

    private int mountId;
    private MountType mountType;

}
