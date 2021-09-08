package ru.finex.ws.l2.persistence;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.finex.core.service.DbSessionService;
import ru.finex.gs.concurrent.service.CallableServiceTask;
import ru.finex.ws.l2.model.Gender;
import ru.finex.ws.l2.model.PlayerAppearanceClass;
import ru.finex.ws.l2.model.PlayerRace;
import ru.finex.ws.l2.model.PvpMode;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.gs.service.concurrent.ServiceExecutorService;
import ru.finex.core.persistence.PersistenceService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PlayerPersistenceService implements PersistenceService<PlayerEntity> {

    private final ServiceExecutorService executorService;
    private final DbSessionService sessionService;

    @Override
    public PlayerEntity persist(PlayerEntity entity) {
        try {
            return executorService.<PlayerEntity>execute(new CallableServiceTask<>(() -> {
                Session session = sessionService.getSession();
                Transaction transaction = session.beginTransaction();
                // ...
                transaction.commit();
                return entity;
            })).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerEntity restore(int gameObjectPersistenceId) {
        return PlayerEntity.builder()
            .persistenceId(gameObjectPersistenceId)
            .appearanceClass(PlayerAppearanceClass.FIGHTER)
            .faceType(0)
            .hairColor(0)
            .hairType(0)
            .name("PlayerGameObject")
            .nameColor(0x000000)
            .pvpMode(PvpMode.NONE)
            .race(PlayerRace.HUMAN)
            .gender(Gender.MALE)
            .title("")
            .titleColor(0x000000)
            .build();
    }

}
