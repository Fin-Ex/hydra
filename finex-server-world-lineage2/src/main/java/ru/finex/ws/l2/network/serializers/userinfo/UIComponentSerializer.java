package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

/**
 * @author m0nster.mind
 */
public interface UIComponentSerializer {

    void writeComponent(UserInfoDto dto, ByteBuf buffer);

    default boolean isSized() {
        return true;
    }

}
