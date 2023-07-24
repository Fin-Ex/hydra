package ru.finex.auth.hydra.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayOkDto implements NetworkDto {

    private long gameSessionKey;

}
