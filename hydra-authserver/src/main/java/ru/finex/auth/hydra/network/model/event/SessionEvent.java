package ru.finex.auth.hydra.network.model.event;

import ru.finex.auth.hydra.network.GameSession;

/**
 * TODO m0nster.mind: move to core
 * @author m0nster.mind
 */
public interface SessionEvent {

    GameSession getSession();

}
