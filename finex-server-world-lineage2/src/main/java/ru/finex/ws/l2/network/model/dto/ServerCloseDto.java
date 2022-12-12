package ru.finex.ws.l2.network.model.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerCloseDto implements NetworkDto {

    public static final ServerCloseDto INSTANCE = new ServerCloseDto();

}
