package ru.finex.ws.l2.component.player;

import lombok.Getter;
import ru.finex.gs.model.Client;
import ru.finex.gs.model.component.AbstractComponent;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
public class ClientComponent extends AbstractComponent {

    @Inject @Getter
    private Client client;

}
