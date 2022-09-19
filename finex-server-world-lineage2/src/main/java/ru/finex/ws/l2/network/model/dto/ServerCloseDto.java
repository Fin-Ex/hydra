package ru.finex.ws.l2.network.model.dto;

import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
public class ServerCloseDto implements NetworkDto {

    public static final ServerCloseDto INSTANCE = new ServerCloseDto();

    private ServerCloseDto() {
    }

}
