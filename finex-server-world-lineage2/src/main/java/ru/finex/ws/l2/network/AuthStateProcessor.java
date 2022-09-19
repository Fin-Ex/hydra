package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventBus;
import ru.finex.ws.l2.network.model.event.SessionConnected;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.network.session.GameClientState;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AuthStateProcessor {

    private final OutcomePacketBuilderService packetBuilderService;

    @Inject
    public void registerListeners(EventBus<GameSessionEvent> eventBus) {
        eventBus.subscribe()
            .cast(SessionConnected.class)
            .forEach(this::clientAuthed);
    }

    private void clientAuthed(SessionConnected evt) {
        GameClient client = evt.getSession();
//        if (!clientAuth.isAuthed()) {
//            client.sendPacket(packetBuilderService.authLoginFail(AuthFailReason.SYSTEM_ERROR_LOGIN_LATER));
//            client.closeNow();
//            return;
//        }

        client.setState(GameClientState.AUTHED);
        client.sendPacket(packetBuilderService.charSelectInfo(
            client.getLogin(),
            /*client.getSessionId().playOkID1*/ 0x00
        ));
    }

}
