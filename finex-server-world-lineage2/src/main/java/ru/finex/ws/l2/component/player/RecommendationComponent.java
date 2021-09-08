package ru.finex.ws.l2.component.player;

import lombok.Getter;
import lombok.Setter;
import ru.finex.gs.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
public class RecommendationComponent extends AbstractComponent {

    @Getter @Setter
    private int left;
    @Getter @Setter
    private int collect;

}
