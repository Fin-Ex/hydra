package ru.finex.auth.l2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RIdGenerator;
import org.redisson.api.RMapCache;
import ru.finex.auth.l2.network.GameSession;
import ru.finex.auth.l2.network.OutcomePacketBuilderService;
import ru.finex.auth.l2.network.model.event.SessionConnected;
import ru.finex.auth.l2.network.model.event.SessionDisconnected;
import ru.finex.auth.l2.network.model.event.SessionEvent;
import ru.finex.auth.service.AuthService;
import ru.finex.core.cluster.AtomicInteger;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.cluster.Map;
import ru.finex.core.cluster.impl.Clustered;
import ru.finex.core.events.EventBus;
import ru.finex.core.hocon.ConfigResource;
import ru.finex.transport.l2.model.dto.AuthSession;
import ru.finex.transport.l2.service.auth.AuthSessionService;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class SessionService implements AuthSessionService {

    private final OutcomePacketBuilderService packetBuilderService;
    private final AuthService authService;

    @Clustered(ACTIVE_SESSIONS)
    private Map<Integer, AuthSession> activeSessions;

    private RMapCache<String, AuthSession> authorizedSessions;

    @Clustered
    private AtomicInteger sessionGenerator;

    private RIdGenerator sessionKeyGenerator;
    private RIdGenerator worldSessionKeyGenerator;

    @ConfigResource
    private long authorizedSessionTtl;

    @Inject
    private void subscribe(EventBus<SessionEvent> eventBus) {
        eventBus.subscribe()
            .cast(SessionConnected.class)
            .forEach(this::onSessionConnected);

        eventBus.subscribe()
            .cast(SessionDisconnected.class)
            .forEach(this::onSessionDisconnected);
    }

    @Inject
    private void authorizedSessions(ClusterService clusterService) {
        authorizedSessions = clusterService.getClient()
            .getMapCache(AUTHORIZED_SESSIONS);
        clusterService.registerManagedResource(authorizedSessions);
    }

    @Inject
    private void sessionKeyGenerators(ClusterService clusterService) {
        sessionKeyGenerator = clusterService.getClient()
            .getIdGenerator(clusterService.getName(getClass(), "sessionKeyGenerator"));
        clusterService.registerManagedResource(sessionKeyGenerator);

        worldSessionKeyGenerator = clusterService.getClient()
            .getIdGenerator(clusterService.getName(getClass(), "worldSessionKeyGenerator"));
        clusterService.registerManagedResource(worldSessionKeyGenerator);
    }

    @Override
    public AuthSession getActiveSession(int sessionId) {
        return activeSessions.get(sessionId);
    }

    public boolean authorizeSession(String login, AuthSession session) {
        return authorizedSessions.fastPutIfAbsent(login, session, authorizedSessionTtl, TimeUnit.SECONDS);
    }

    public void revokeAuthorize(String login) {
        authorizedSessions.fastRemove(login);
    }

    @Override
    public AuthSession getAuthorizedSession(String login) {
        return authorizedSessions.get(login);
    }

    private void onSessionConnected(SessionConnected evt) {
        GameSession session = evt.getSession();
        AuthSession data = session.getData();

        int sessionId = sessionGenerator.incrementAndGet();
        data.setSessionId(sessionId);
        data.setSessionKey(sessionKeyGenerator.nextId());
        data.setWorldSessionKey(worldSessionKeyGenerator.nextId());

        AuthSession associated = activeSessions.putIfAbsent(sessionId, data);
        if (associated != null) {
            log.error("Generated session ID ({}) already associated with session: {}", session, associated);
            session.closeNow();
        }

        session.sendPacket(packetBuilderService.init(session));
    }

    private void onSessionDisconnected(SessionDisconnected evt) {
        GameSession session = evt.getSession();
        AuthSession data = session.getData();

        activeSessions.remove(data.getSessionId());
        authService.logoutUser(session.getLogin());
        log.debug("{} closed.", session);
    }

}
