package ru.finex.auth.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.network.model.event.SessionConnected;
import ru.finex.auth.l2.network.model.event.SessionEvent;
import ru.finex.core.events.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class SessionStarter {

    private final OutcomePacketBuilderService packetBuilderService;

    @Inject
    public void registerListeners(EventBus<SessionEvent> eventBus) {
        eventBus.subscribe()
            .cast(SessionConnected.class)
            .forEach(this::initSession);
    }

    private void initSession(SessionConnected evt) {
        GameSession session = evt.getSession();
        session.sendPacket(packetBuilderService.init(session));
    }

}
