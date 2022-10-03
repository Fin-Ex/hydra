package ru.finex.transport.l2.service.auth;

import ru.finex.transport.l2.model.dto.AuthSession;

/**
 * @author m0nster.mind
 */
public interface AuthSessionService {
    String ACTIVE_SESSIONS = "auth@AuthSessionService#activeSessions";
    String AUTHORIZED_SESSIONS = "auth@AuthSessionService#authorizedSessions";

    AuthSession getActiveSession(int sessionId);
    AuthSession getAuthorizedSession(String login);

}
