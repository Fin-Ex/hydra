package ru.finex.ws.l2.network.model.dto;

import lombok.NoArgsConstructor;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@NoArgsConstructor
@NetworkCommandScoped
public class RequestGotoLobbyDto implements NetworkDto {

    public static final RequestGotoLobbyDto INSTANCE = new RequestGotoLobbyDto();

}
