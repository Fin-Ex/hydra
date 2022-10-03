package ru.finex.ws.l2.component.player;

import lombok.Data;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class RecommendationComponent extends AbstractComponent {

    private int left;
    private int collect;

}
