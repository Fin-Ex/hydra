package ru.finex.ws.l2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.ws.l2.model.entity.ClanEntity;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.ws.l2.model.entity.PositionEntity;
import ru.finex.ws.l2.model.entity.StatusEntity;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectedAvatarDto {

    private PlayerEntity player;
    private ClanEntity clan;
    private PositionEntity position;
    private StatusEntity status;

}
