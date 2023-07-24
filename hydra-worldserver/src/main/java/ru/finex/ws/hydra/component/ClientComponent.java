package ru.finex.ws.hydra.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.finex.ws.hydra.network.session.GameClient;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientComponent extends AbstractComponent {

    private GameClient client;

}
