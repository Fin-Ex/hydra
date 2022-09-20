package ru.finex.auth.l2.network.model.event;

import ru.finex.auth.l2.network.GameSession;

/**
 * TODO m0nster.mind: move to core
 * @author m0nster.mind
 */
public interface SessionEvent {

    GameSession getSession();

}
