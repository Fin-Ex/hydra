package sf.finex.model.component.player;

import lombok.Data;
import sf.finex.model.component.AbstractComponent;
import sf.finex.model.player.PlayerEquipLimit;

/**
 * @author m0nster.mind
 */
@Data
public class VisualEquipComponent extends AbstractComponent {

    private final int[] visualEquip = new int[PlayerEquipLimit.EQUIP_COUNT];
    private final int[] visualAugment = new int[PlayerEquipLimit.EQUIP_COUNT];

}
