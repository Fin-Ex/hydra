package ru.finex.gs.model.component.player;

import lombok.Data;
import ru.finex.gs.model.PlayerEquipLimit;
import ru.finex.gs.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class VisualEquipComponent extends AbstractComponent {

    private final int[] visualEquip = new int[PlayerEquipLimit.EQUIP_COUNT];
    private final int[] visualAugment = new int[PlayerEquipLimit.EQUIP_COUNT];

}
