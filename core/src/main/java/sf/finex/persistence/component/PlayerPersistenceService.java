package sf.finex.persistence.component;

import sf.finex.model.GameObject;
import sf.finex.model.entity.PlayerEntity;
import sf.finex.model.player.PlayerAppearanceClass;
import sf.finex.model.player.PvpMode;
import sf.finex.service.persistence.PersistenceService;
import sf.l2j.gameserver.model.base.ClassRace;
import sf.l2j.gameserver.model.base.Sex;

/**
 * @author m0nster.mind
 */
public class PlayerPersistenceService implements PersistenceService<PlayerEntity> {

    @Override
    public PlayerEntity persist(PlayerEntity entity) {
        return entity;
    }

    @Override
    public PlayerEntity restore(GameObject gameObject) {
        int persistenceId = gameObject.getPersistenceId();

        return PlayerEntity.builder()
            .persistenceId(persistenceId)
            .appearanceClass(PlayerAppearanceClass.FIGHTER)
            .faceType(0)
            .hairColor(0)
            .hairType(0)
            .name("PlayerGameObject")
            .nameColor(0x000000)
            .pvpMode(PvpMode.NONE)
            .race(ClassRace.HUMAN)
            .sex(Sex.MALE)
            .title("")
            .titleColor(0x000000)
            .build();
    }

}
