package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventBus;
import ru.finex.core.model.GameObject;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.l2.network.model.event.ClientDisconnected;
import ru.finex.ws.l2.network.model.event.ClientEvent;
import ru.finex.ws.model.Client;
import ru.finex.ws.service.ClientService;
import ru.finex.ws.service.GameObjectService;

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
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ClientServiceImpl implements ClientService {

    private final List<L2GameClient> sessions = new ArrayList<>();
    private final ReadWriteLock sessionRwLock = new ReentrantReadWriteLock();
    private final GameObjectService gameObjectService;

    @Inject
    public void registerListeners(@Named("Network") EventBus<ClientEvent> eventBus) {
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

        GameObject gameObject = client.getGameObject();
        if (gameObject != null) {
            gameObjectService.destroyObject(gameObject);
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
