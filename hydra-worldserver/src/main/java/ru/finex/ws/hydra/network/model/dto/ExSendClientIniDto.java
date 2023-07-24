package ru.finex.ws.hydra.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NetworkCommandScoped
public class ExSendClientIniDto implements NetworkDto {

    private String ini;

}
