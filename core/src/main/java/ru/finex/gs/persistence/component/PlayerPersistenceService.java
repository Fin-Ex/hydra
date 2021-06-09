package ru.finex.gs.persistence.component;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.finex.gs.model.PlayerAppearanceClass;
import ru.finex.gs.model.PlayerRace;
import ru.finex.gs.model.PvpMode;
import ru.finex.gs.model.Sex;
import ru.finex.gs.model.entity.PlayerEntity;
import ru.finex.gs.service.DbSessionService;
import ru.finex.gs.service.persistence.PersistenceService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PlayerPersistenceService implements PersistenceService<PlayerEntity> {

    private final DbSessionService sessionService;

    @Override
    public PlayerEntity persist(PlayerEntity entity) {
        Session session = sessionService.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            //...

            transaction.commit();
        } finally {
            session.close();
        }
        return entity;
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
            .sex(Sex.MALE)
            .title("")
            .titleColor(0x000000)
            .build();
    }

}
