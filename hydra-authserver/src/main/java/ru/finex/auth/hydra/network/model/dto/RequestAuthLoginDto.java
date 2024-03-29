package ru.finex.auth.hydra.network.model.dto;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NetworkCommandScoped
public class RequestAuthLoginDto implements NetworkDto {

    private String login;
    private String password;
    /** NcSoft OTP code. */
    private int ncOtp;

    private int sessionId;
    private int unk1;
    private int unk2;
    private int unk3;
    private int unk4;

}
