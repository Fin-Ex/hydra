package ru.finex.gs.model.component.player;

import lombok.Data;
import ru.finex.gs.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class RecommendationComponent extends AbstractComponent {

    private int left;
    private int collect;

}
