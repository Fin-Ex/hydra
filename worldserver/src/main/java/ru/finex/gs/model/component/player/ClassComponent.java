package ru.finex.gs.model.component.player;

import lombok.Data;
import ru.finex.gs.model.component.AbstractComponent;
import sf.l2j.gameserver.model.base.ClassId;

/**
 * @author m0nster.mind
 */
@Data
public class ClassComponent extends AbstractComponent {

    private ClassId classId = ClassId.HumanFighter;

}
