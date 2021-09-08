package ru.finex.ws.l2.model.dto;

import lombok.Data;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.core.persistence.PersistenceObject;
import ru.finex.ws.l2.model.entity.ClanEntity;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.ws.l2.model.entity.PositionEntity;
import ru.finex.ws.l2.model.entity.StatusEntity;
import ru.finex.ws.l2.persistence.ClanPersistenceService;
import ru.finex.ws.l2.persistence.PlayerPersistenceService;
import ru.finex.ws.l2.persistence.PositionPersistence;
import ru.finex.ws.l2.persistence.StatusPersistenceService;

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
