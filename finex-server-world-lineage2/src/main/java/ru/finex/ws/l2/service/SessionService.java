package ru.finex.ws.l2.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.cluster.Map;
import ru.finex.core.cluster.impl.Clustered;
import ru.finex.core.cluster.impl.ClusteredService;
import ru.finex.core.events.EventBus;
import ru.finex.transport.l2.model.dto.AuthSession;
import ru.finex.transport.l2.model.dto.WorldSession;
import ru.finex.transport.l2.service.auth.AuthSessionService;
import ru.finex.transport.l2.service.world.WorldSessionService;
import ru.finex.ws.l2.model.exception.InvalidSessionException;
import ru.finex.ws.l2.network.model.dto.AuthKeyDto;
import ru.finex.ws.l2.network.model.event.GameSessionEvent;
import ru.finex.ws.l2.network.model.event.SessionDisconnected;
import ru.finex.ws.l2.network.session.GameClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@ClusteredService("server-${config['ru.finex.ws.l2.service.SessionService.serverId']}")
public class SessionService implements WorldSessionService {

    @Clustered(ACTIVE_SESSIONS)
    private Map<Integer, WorldSession> sessions;

    private RMapCache<String, AuthSession> authorizedSessions;

    @Inject
    public SessionService(EventBus<GameSessionEvent> eventBus) {
        eventBus.subscribe()
            .cast(SessionDisconnected.class)
            .forEach(this::onSessionDisconnected);
    }

    @Inject
    private void authorizedSessions(ClusterService clusterService) {
        authorizedSessions = clusterService.getClient()
            .getMapCache(AuthSessionService.AUTHORIZED_SESSIONS);
        clusterService.registerManagedResource(authorizedSessions);
    }

    public int authorizeSession(AuthKeyDto authDto) throws InvalidSessionException {
        AuthSession authSession = authorizedSessions.get(authDto.getLogin());
        if (authSession == null) {
            throw new InvalidSessionException("Authorized session not found for login: " + authDto.getLogin());
        }

        if (authDto.getAuthSessionKey() != authSession.getSessionKey() ||
            authDto.getWorldSessionKey() != authSession.getWorldSessionKey()) {
            throw new InvalidSessionException("Invalid session keys");
        }

        WorldSession worldSession = new WorldSession();
        worldSession.setSessionId(authSession.getSessionId());
        worldSession.setSessionKey(authSession.getWorldSessionKey());
        worldSession.setUserId(authSession.getUserId());

        int sessionId = authSession.getSessionId();
        if (sessions.putIfAbsent(sessionId, worldSession) != null) {
            throw new InvalidSessionException("Session already authorized: " + sessionId);
        }

        return sessionId;
    }

    private void onSessionDisconnected(SessionDisconnected evt) {
        GameClient session = evt.getSession();
        String login = session.getLogin();
        if (StringUtils.isBlank(login)) {
            return;
        }

        WorldSession data = session.getData();
        sessions.remove(data.getSessionId());
        authorizedSessions.fastRemove(login);
    }

    @Override
    public WorldSession getActiveSession(int sessionId) {
        return sessions.get(sessionId);
    }

}
