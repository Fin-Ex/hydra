package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
@NetworkCommandScoped
public class RestartResponseDto implements NetworkDto {

    public static final RestartResponseDto TRUE = new RestartResponseDto(true);
    public static final RestartResponseDto FALSE = new RestartResponseDto(false);

    private Boolean result;
}
