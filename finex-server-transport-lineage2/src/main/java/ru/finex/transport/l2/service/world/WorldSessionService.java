package ru.finex.transport.l2.service.world;

import ru.finex.transport.l2.model.dto.WorldSession;

/**
 * @author m0nster.mind
 */
public interface WorldSessionService {
    String ACTIVE_SESSIONS = "world@WorldSessionService#activeSessions";

    WorldSession getActiveSession(int sessionId);

}
