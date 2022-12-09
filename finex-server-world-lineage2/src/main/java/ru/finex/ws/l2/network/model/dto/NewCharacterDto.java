package ru.finex.ws.l2.network.model.dto;

import lombok.Data;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@NetworkCommandScoped
public class NewCharacterDto implements NetworkDto {

    public static final NewCharacterDto INSTANCE = new NewCharacterDto();

}
