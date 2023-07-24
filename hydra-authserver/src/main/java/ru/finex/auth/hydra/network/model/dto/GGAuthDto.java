package ru.finex.auth.hydra.network.model.dto;

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
public class GGAuthDto implements NetworkDto {

    private int sessionId;
    private int unk1;
    private int unk2;
    private int unk3;
    private int unk4;

}
