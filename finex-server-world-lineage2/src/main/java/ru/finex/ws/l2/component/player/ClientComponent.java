package ru.finex.ws.l2.component.player;

import lombok.Getter;
import ru.finex.ws.model.Client;
import ru.finex.ws.model.component.AbstractComponent;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
public class ClientComponent extends AbstractComponent {

    @Inject @Getter
    private Client client;

}
