package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.ws.l2.model.entity.AvatarPrototypeView;

import java.util.List;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCharacterSuccessDto implements NetworkDto {

    private List<AvatarPrototypeView> prototypes;

}
