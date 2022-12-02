package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UISlotComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        buffer.writeByte(0x00); // FIXME finfan: _player.getInventory().getTalismanSlots()
        buffer.writeByte(0x00); // FIXME finfan: _player.getInventory().getBroochJewelSlots()
        buffer.writeByte(0x00); // FIXME finfan: Confirmed _player.getTeam().getId()
        buffer.writeByte(0x00); // FIXME finfan: (1 = Red, 2 = White, 3 = White Pink) dotted ring on the floor
        buffer.writeByte(0x00);
        buffer.writeByte(0x00);
        buffer.writeByte(0x00);
    }

}
