package ru.finex.ws.l2.network.model.event;

import ru.finex.ws.l2.network.session.GameClient;

/**
 * @author m0nster.mind
 */
public interface GameSessionEvent {

    GameClient getSession();

}
