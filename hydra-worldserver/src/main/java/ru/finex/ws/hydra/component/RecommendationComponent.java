package ru.finex.ws.hydra.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RecommendationComponent extends AbstractComponent {

    private int left;
    private int collect;

}
