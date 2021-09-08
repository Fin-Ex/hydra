package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventBus;
import ru.finex.ws.l2.auth.model.AuthFailReason;
import ru.finex.ws.l2.auth.model.event.ClientAuth;
import ru.finex.ws.l2.network.model.GameClientState;
import ru.finex.ws.l2.network.model.L2GameClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AuthStateProcessor {

    private final OutcomePacketBuilderService packetBuilderService;

    @Inject
    public void registerListeners(@Named("Network") EventBus eventBus) {
        eventBus.subscribe()
            .cast(ClientAuth.class)
            .forEach(this::clientAuthed);
    }

    private void clientAuthed(ClientAuth clientAuth) {
        L2GameClient client = (L2GameClient) clientAuth.getClient();
        if (!clientAuth.isAuthed()) {
            client.sendPacket(packetBuilderService.authLoginFail(AuthFailReason.SYSTEM_ERROR_LOGIN_LATER));
            client.closeNow();
            return;
        }

        client.setState(GameClientState.AUTHED);
        client.sendPacket(packetBuilderService.charSelectInfo(client.getLogin(), client.getSessionId().playOkID1));
    }

}
