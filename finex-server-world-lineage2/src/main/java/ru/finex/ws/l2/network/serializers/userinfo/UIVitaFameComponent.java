package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIVitaFameComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        buffer.writeIntLE(0); // FIXME finfan: _player.getVitalityPoints()
        buffer.writeByte(0x00); // FIXME finfan: Vita Bonus
        buffer.writeIntLE(0x00); // FIXME finfan: _player.getFame()
        buffer.writeIntLE(0x00); // FIXME finfan: _player.getRaidPoints()
    }

}
