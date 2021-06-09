package ru.finex.gs.model.component.player;

import lombok.Getter;
import ru.finex.gs.model.component.AbstractComponent;
import sf.l2j.gameserver.network.L2GameClient;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
public class ClientComponent extends AbstractComponent {

    @Inject @Getter
    private L2GameClient client;

}
