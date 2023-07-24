package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

/**
 * @author m0nster.mind
 */
public interface UIComponentSerializer {

    void writeComponent(UserInfoDto dto, ByteBuf buffer);

    default boolean isSized() {
        return true;
    }

}
