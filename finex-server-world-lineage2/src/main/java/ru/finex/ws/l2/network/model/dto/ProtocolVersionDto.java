package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.ws.l2.network.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolVersionDto implements NetworkDto {

    private int version;

}
