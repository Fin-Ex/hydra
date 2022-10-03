package ru.finex.ws.l2.service;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventBus;
import ru.finex.core.model.GameObject;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.ws.l2.network.model.event.GameSessionEvent;
import ru.finex.ws.l2.network.model.event.SessionDisconnected;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AutoSaveService {

    private final GameObjectPersistenceService persistenceService;

    @Inject
    private void registerListener(EventBus<GameSessionEvent> eventBus) {
        eventBus.subscribe()
            .cast(SessionDisconnected.class)
            .forEach(this::onSessionDisconnected);
    }

    private void onSessionDisconnected(SessionDisconnected evt) {
        GameObject gameObject = evt.getSession().getGameObject();
        if (gameObject == null) {
            return;
        }

        persistenceService.persist(gameObject);
    }

}
