package ru.finex.auth.hydra.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;

import java.security.PublicKey;
import javax.crypto.SecretKey;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitDto implements NetworkDto {

    private int sessionId;
    private PublicKey publicKey;
    private SecretKey blowfishKey;
    private int protocolRevision;

}
