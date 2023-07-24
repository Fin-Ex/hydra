package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIHeroComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        buffer.writeIntLE(0x00);
        buffer.writeShortLE(0x00);
        buffer.writeByte(0x00); //_player.isTrueHero() ? 100 : 0x00
    }

}
