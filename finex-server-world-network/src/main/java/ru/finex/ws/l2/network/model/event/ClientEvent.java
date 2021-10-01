package ru.finex.ws.l2.network.model.event;

import ru.finex.core.pool.Cleanable;
import ru.finex.ws.model.Client;

/**
 * @author m0nster.mind
 */
public interface ClientEvent extends Cleanable {

    Client getClient();

}
