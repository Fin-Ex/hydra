package ru.finex.ws.hydra.network;

import lombok.RequiredArgsConstructor;
import ru.finex.ws.hydra.network.session.GameClient;
import ru.finex.ws.service.ClientService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class NetworkClientFactory {

    private final ClientService clientService;
    private final Provider<GameClient> gameClientProvider;

    public GameClient create() {
        var session = gameClientProvider.get();
        clientService.addSession(session);
        return session;
    }

}
