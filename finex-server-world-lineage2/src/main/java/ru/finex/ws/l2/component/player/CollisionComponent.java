package ru.finex.ws.l2.component.player;

import lombok.Getter;
import lombok.Setter;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
public class CollisionComponent extends AbstractComponent {

    @Getter @Setter
    private double width = 9;

    @Getter @Setter
    private double height = 23;

}
