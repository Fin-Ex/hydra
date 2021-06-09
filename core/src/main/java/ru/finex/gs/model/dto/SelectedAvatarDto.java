package ru.finex.gs.model.dto;

import lombok.Data;
import ru.finex.gs.model.entity.ClanEntity;
import ru.finex.gs.model.entity.PlayerEntity;
import ru.finex.gs.model.entity.PositionEntity;
import ru.finex.gs.model.entity.StatusEntity;

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
