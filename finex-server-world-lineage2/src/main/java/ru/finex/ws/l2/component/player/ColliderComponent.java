package ru.finex.ws.l2.component.player;

import lombok.Data;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class ColliderComponent extends AbstractComponent {

    private double width;
    private double height;

}
