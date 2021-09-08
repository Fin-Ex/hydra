package ru.finex.ws.l2.network.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.finex.ws.l2.network.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@Data
@Builder
public class AuthKeyDto implements NetworkDto {

    private String login;
    private int playKey1;
    private int playKey2;
    private int loginKey1;
    private int loginKey2;

}
