package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.model.entity.ActiveClassComponentEntity;
import ru.finex.ws.hydra.model.entity.PlayerComponentEntity;
import ru.finex.ws.hydra.network.SerializerHelper;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIBasicInfoComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        PlayerComponentEntity player = dto.getPlayerComponent().getEntity();
        ActiveClassComponentEntity activeClass = dto.getClassComponent().getEntity();

        SerializerHelper.writeString(buffer, player.getName());
        buffer.writeByte(0x00); // isGM
        buffer.writeByte(player.getRace().ordinal());
        buffer.writeByte(player.getGender().ordinal());
        buffer.writeIntLE(player.getAppearanceClass().getNetworkId(player.getRace(), player.getGender()));
        buffer.writeIntLE(activeClass.getClassId().getId());
        buffer.writeByte(activeClass.getLevel());
    }

}
