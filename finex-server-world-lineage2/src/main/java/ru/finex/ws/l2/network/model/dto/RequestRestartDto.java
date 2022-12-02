package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@NetworkCommandScoped
public class RequestRestartDto implements NetworkDto {

    public static final RequestRestartDto INSTANCE = new RequestRestartDto();

}
