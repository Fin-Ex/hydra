package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.ServerContext;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.service.ClientService;
import sf.l2j.commons.mmocore.IClientFactory;
import sf.l2j.commons.mmocore.MMOConnection;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class NetworkClientFactory implements IClientFactory<L2GameClient> {

    private final ClientService clientService;
    private final ServerContext context;

    @Override
    public L2GameClient create(MMOConnection<L2GameClient> con) {
        L2GameClient client = new L2GameClient(con);
        context.getInjector().injectMembers(client);
        clientService.addSession(client);
        return client;
    }

}
