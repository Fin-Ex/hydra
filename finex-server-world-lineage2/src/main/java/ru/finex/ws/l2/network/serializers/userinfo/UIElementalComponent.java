package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIElementalComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        buffer.writeShortLE(0x00); // defense attribute FIRE
        buffer.writeShortLE(0x00); // defense attribute WATER
        buffer.writeShortLE(0x00); // defense attribute WIND
        buffer.writeShortLE(0x00); // defense attribute EARTH
        buffer.writeShortLE(0x00); // defense attribute HOLY
        buffer.writeShortLE(0x00); // defense attribute DARK
    }

}
