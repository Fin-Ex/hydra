package ru.finex.transport.hydra.service.world;

import ru.finex.transport.hydra.model.dto.WorldSession;

/**
 * @author m0nster.mind
 */
public interface WorldSessionService {
    String ACTIVE_SESSIONS = "world@WorldSessionService#activeSessions";

    WorldSession getActiveSession(int sessionId);

}
