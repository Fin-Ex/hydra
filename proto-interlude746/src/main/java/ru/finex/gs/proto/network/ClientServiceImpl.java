package ru.finex.gs.proto.network;

import ru.finex.core.events.EventBus;
import ru.finex.gs.model.Client;
import ru.finex.gs.model.event.ClientDisconnected;
import ru.finex.gs.service.ClientService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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

    @Inject
    public void startClientNotifier(@Named("Service") ScheduledExecutorService executorService) {
        // 8.3 ticks per sec
        executorService.scheduleAtFixedRate(this::notifyClients, 120, 120, TimeUnit.MILLISECONDS);
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

    public List<L2GameClient> getRealSessions() {
        Lock lock = sessionRwLock.readLock();
        lock.lock();
        try {
            return new ArrayList<>(sessions);
        } finally {
            lock.unlock();
        }
    }

    private void notifyClients() {
        // исполнение входящих пакетов для клиентов, которые еще не находятся в мире
        getRealSessions()
            .stream()
            .map(L2GameClient::getServicePacketExecutor)
            .forEach(ServicePacketExecutor::executeQueue);
    }

}
