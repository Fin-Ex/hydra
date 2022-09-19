package ru.finex.ws.l2.service;

import manifold.util.concurrent.ConcurrentHashSet;
import ru.finex.ws.model.ClientSession;
import ru.finex.ws.service.ClientService;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ClientServiceImpl implements ClientService {

    private final ConcurrentHashSet<ClientSession> sessions = new ConcurrentHashSet<>();

    @Override
    public void addSession(ClientSession client) {
        sessions.add(client);
    }

    @Override
    public void removeSession(ClientSession client) {
        sessions.remove(client);
    }

    @Override
    public List<ClientSession> getSessions() {
        return new ArrayList<>(sessions);
    }
}
