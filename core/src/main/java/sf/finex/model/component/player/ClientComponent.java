package sf.finex.model.component.player;

import lombok.Getter;
import sf.finex.model.component.AbstractComponent;
import sf.l2j.gameserver.network.L2GameClient;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
public class ClientComponent extends AbstractComponent {

    @Inject @Getter
    private L2GameClient client;

}
