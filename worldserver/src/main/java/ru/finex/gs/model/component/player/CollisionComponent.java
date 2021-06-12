package ru.finex.gs.model.component.player;

import lombok.Data;
import ru.finex.gs.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class CollisionComponent extends AbstractComponent {

    private double width = 9;
    private double height = 23;

}
