package ru.finex.ws.l2.component.player;

import lombok.Getter;
import ru.finex.ws.model.component.AbstractComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author m0nster.mind
 */
public class CubicComponent extends AbstractComponent {

    @Getter
    private List<Integer> cubics = new ArrayList<>();

}
