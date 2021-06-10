package ru.finex.gs.model.component.player;

import lombok.Data;
import ru.finex.gs.model.component.AbstractComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author m0nster.mind
 */
@Data
public class CubicComponent extends AbstractComponent {

    private List<Integer> cubics = new ArrayList<>();

}
