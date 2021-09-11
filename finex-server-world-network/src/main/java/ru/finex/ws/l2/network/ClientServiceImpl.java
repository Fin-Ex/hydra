package ru.finex.ws.l2.network;

import ru.finex.core.events.EventBus;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.l2.network.model.event.ClientDisconnected;
import ru.finex.ws.model.Client;
import ru.finex.ws.service.ClientService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ClientServiceImpl implements ClientService {

    private final List<L2GameClient> sessions = new ArrayList<>();
    private final ReadWriteLock sessionRwLock = new ReentrantReadWriteLock();

    @Inject
    public void registerListeners(@Named("Network") EventBus eventBus) {
        eventBus.subscribe()
            .cast(ClientDisconnected.class)
            .map(ClientDisconnected::getClient)
            .forEach(this::removeSession);
    }

    @Override
    public void addSession(Client client) {
        Lock lock = sessionRwLock.writeLock();
        lock.lock();
        try {
            sessions.add((L2GameClient) client);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeSession(Client client) {
        Lock lock = sessionRwLock.writeLock();
        lock.lock();
        try {
            sessions.remove(client);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Client> getSessions() {
        Lock lock = sessionRwLock.readLock();
        lock.lock();
        try {
            return new ArrayList<>(sessions);
        } finally {
            lock.unlock();
        }
    }

}
