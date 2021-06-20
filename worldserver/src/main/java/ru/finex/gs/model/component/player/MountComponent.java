package ru.finex.gs.model.component.player;

import lombok.Data;
import ru.finex.gs.model.MountType;
import ru.finex.gs.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class MountComponent extends AbstractComponent {

    private int mountId;
    private MountType mountType;

}
