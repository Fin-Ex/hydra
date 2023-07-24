package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIStatusFlagComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        buffer.writeByte(dto.getMountComponent().getMountType().ordinal());
        buffer.writeByte(dto.getStoreComponent().getStoreType().ordinal());
        buffer.writeByte(0x00); // _player.hasDwarvenCraft() || (_player.getSkillLevel(248) > 0) ? 1 : 0
        buffer.writeByte(0x00); // _player.getAbilityPointsUsed()
    }

}
