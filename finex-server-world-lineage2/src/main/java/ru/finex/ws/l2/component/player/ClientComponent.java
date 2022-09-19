package ru.finex.ws.l2.component.player;

import lombok.Data;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class ClientComponent extends AbstractComponent {

    private GameClient client;

}
