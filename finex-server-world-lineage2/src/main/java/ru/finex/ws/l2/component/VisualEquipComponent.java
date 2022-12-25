package ru.finex.ws.l2.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.finex.ws.l2.model.PlayerEquipLimit;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisualEquipComponent extends AbstractComponent {

    private final int[] visualEquip = new int[PlayerEquipLimit.EQUIP_COUNT];
    private final int[] visualAugment = new int[PlayerEquipLimit.EQUIP_COUNT];

}
