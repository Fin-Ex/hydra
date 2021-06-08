package sf.finex.model.component.player;

import lombok.Data;
import sf.finex.model.component.AbstractComponent;
import sf.finex.model.entity.ClanEntity;

/**
 * @author m0nster.mind
 */
@Data
public class ClanComponent extends AbstractComponent {

    private ClanEntity clanEntity;

}
