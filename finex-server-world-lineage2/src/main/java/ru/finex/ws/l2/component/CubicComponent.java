package ru.finex.ws.l2.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.finex.ws.model.component.AbstractComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author m0nster.mind
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CubicComponent extends AbstractComponent {

    private List<Integer> cubics = new ArrayList<>();

}
