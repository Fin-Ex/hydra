package ru.finex.ws.l2.model.dto;

import lombok.Data;
import ru.finex.ws.l2.model.entity.ClanEntity;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.ws.l2.model.entity.PositionEntity;
import ru.finex.ws.l2.model.entity.StatusEntity;

/**
 * @author m0nster.mind
 */
@Data
public class SelectedAvatarDto {

    private PlayerEntity player;
    private ClanEntity clan;
    private PositionEntity position;
    private StatusEntity status;

}
