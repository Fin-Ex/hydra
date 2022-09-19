package ru.finex.ws.l2.network;

import ru.finex.ws.l2.network.session.GameClient;

/**
 * @author m0nster.mind
 */
public interface GameSessionEvent {

    GameClient getSession();

}
