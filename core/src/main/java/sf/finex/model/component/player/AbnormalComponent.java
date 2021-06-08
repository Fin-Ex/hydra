package sf.finex.model.component.player;

import sf.l2j.gameserver.skills.AbnormalEffect;

import java.util.HashSet;
import java.util.Set;

/**
 * @author m0nster.mind
 */
public class AbnormalComponent {

    private Set<AbnormalEffect> abnormals = new HashSet<>();

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
