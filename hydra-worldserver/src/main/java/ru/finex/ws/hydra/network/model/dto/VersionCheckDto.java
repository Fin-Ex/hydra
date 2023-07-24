package ru.finex.ws.hydra.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionCheckDto implements NetworkDto {

    /** Is valid client protocol version. */
    private boolean isValid;
    private byte[] key;
    private int languageId = 1;
    private int serverId;

}
