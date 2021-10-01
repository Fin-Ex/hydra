package ru.finex.ws.l2.component.player;

import lombok.Data;
import lombok.Getter;
import ru.finex.ws.model.Client;
import ru.finex.ws.model.component.AbstractComponent;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@Data
public class ClientComponent extends AbstractComponent {

    private Client client;

}
