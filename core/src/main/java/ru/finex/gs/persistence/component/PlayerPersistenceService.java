package ru.finex.gs.persistence.component;

import ru.finex.gs.model.PlayerAppearanceClass;
import ru.finex.gs.model.PlayerRace;
import ru.finex.gs.model.PvpMode;
import ru.finex.gs.model.Sex;
import ru.finex.gs.model.entity.PlayerEntity;
import ru.finex.gs.service.persistence.PersistenceService;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class PlayerPersistenceService implements PersistenceService<PlayerEntity> {

    @Override
    public PlayerEntity persist(PlayerEntity entity) {
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
