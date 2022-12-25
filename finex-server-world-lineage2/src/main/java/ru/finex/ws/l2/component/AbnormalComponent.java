package ru.finex.ws.l2.component;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.finex.ws.l2.model.AbnormalEffect;
import ru.finex.ws.model.component.AbstractComponent;

import java.util.HashSet;
import java.util.Set;

/**
 * @author m0nster.mind
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class AbnormalComponent extends AbstractComponent {

    private final Set<AbnormalEffect> abnormals = new HashSet<>();

    public int getMask() {
        return abnormals.stream()
            .map(AbnormalEffect::getMask)
            .reduce(0, (e1, e2) -> e1 | e2);
    }

    public boolean add(AbnormalEffect abnormalEffect) {
        return abnormals.add(abnormalEffect);
    }

    public boolean remove(AbnormalEffect abnormalEffect) {
        return abnormals.remove(abnormalEffect);
    }

}
