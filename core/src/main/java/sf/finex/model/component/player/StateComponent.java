package sf.finex.model.component.player;

import lombok.Data;
import sf.finex.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class StateComponent extends AbstractComponent {

    private boolean isSitting;
    private boolean isRunning;
    private boolean isBattleStance;
    private boolean isDeath;
    private boolean isInvisible;
    private boolean isSearchParty;
    private boolean isHeroAura;
    private boolean isNoble;

}
