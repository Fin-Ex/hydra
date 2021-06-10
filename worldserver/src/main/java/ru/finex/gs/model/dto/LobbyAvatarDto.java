package ru.finex.gs.model.dto;

import lombok.Data;
import ru.finex.gs.model.entity.ClanEntity;
import ru.finex.gs.model.entity.PlayerEntity;
import ru.finex.gs.model.entity.PositionEntity;
import ru.finex.gs.model.entity.StatusEntity;
import ru.finex.gs.persistence.PersistenceField;
import ru.finex.gs.persistence.PersistenceObject;
import ru.finex.gs.persistence.component.ClanPersistenceService;
import ru.finex.gs.persistence.component.PlayerPersistenceService;
import ru.finex.gs.persistence.component.PositionPersistence;
import ru.finex.gs.persistence.component.StatusPersistenceService;

/**
 * @author m0nster.mind
 */
@Data
public class LobbyAvatarDto implements PersistenceObject {

    private int persistenceId;

    @PersistenceField(PlayerPersistenceService.class)
    private PlayerEntity player;
    @PersistenceField(ClanPersistenceService.class)
    private ClanEntity clan;
    @PersistenceField(PositionPersistence.class)
    private PositionEntity position;
    @PersistenceField(StatusPersistenceService.class)
    private StatusEntity status;

}
