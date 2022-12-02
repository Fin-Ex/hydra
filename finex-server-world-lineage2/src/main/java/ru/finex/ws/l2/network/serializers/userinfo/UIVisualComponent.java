package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.model.entity.PlayerComponentEntity;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIVisualComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        PlayerComponentEntity player = dto.getPlayerComponent().getEntity();
        buffer.writeIntLE(player.getHairType()); // visual hair type
        buffer.writeIntLE(player.getHairColor()); // visual haoir color
        buffer.writeIntLE(player.getFaceType()); //visual face type
        buffer.writeByte(0x00); //isHairAccessoryEnabled
    }

}
