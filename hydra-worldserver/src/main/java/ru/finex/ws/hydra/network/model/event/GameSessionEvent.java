package ru.finex.ws.hydra.network.model.event;

import ru.finex.ws.hydra.network.session.GameClient;

/**
 * @author m0nster.mind
 */
public interface GameSessionEvent {

    GameClient getSession();

}
