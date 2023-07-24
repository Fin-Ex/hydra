package ru.finex.transport.hydra.service.auth;

import ru.finex.transport.hydra.model.dto.AuthSession;

/**
 * @author m0nster.mind
 */
public interface AuthSessionService {
    String ACTIVE_SESSIONS = "auth@AuthSessionService#activeSessions";
    String AUTHORIZED_SESSIONS = "auth@AuthSessionService#authorizedSessions";

    AuthSession getActiveSession(int sessionId);
    AuthSession getAuthorizedSession(String login);

}
