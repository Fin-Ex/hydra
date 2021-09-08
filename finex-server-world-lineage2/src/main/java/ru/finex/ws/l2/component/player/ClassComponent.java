package ru.finex.ws.l2.component.player;

import lombok.Getter;
import lombok.Setter;
import ru.finex.gs.model.component.AbstractComponent;
import ru.finex.ws.l2.model.ClassId;

/**
 * @author m0nster.mind
 */
public class ClassComponent extends AbstractComponent {

    @Getter @Setter
    private ClassId classId = ClassId.HumanFighter;

}
