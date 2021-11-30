package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.ServerContext;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.service.ClientService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class NetworkClientFactory {

    private final ClientService clientService;
    private final ServerContext context;

    public L2GameClient create() {
        L2GameClient client = new L2GameClient();
        context.getInjector().injectMembers(client);
        clientService.addSession(client);
        return client;
    }

}
