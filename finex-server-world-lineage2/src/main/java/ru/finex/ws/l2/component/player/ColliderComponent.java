package ru.finex.ws.l2.component.player;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class ColliderComponent extends AbstractComponent {

    private double width = 9;
    private double height = 23;

}
