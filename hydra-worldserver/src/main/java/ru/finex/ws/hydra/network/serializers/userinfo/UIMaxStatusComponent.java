package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.model.entity.StatusComponentEntity;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIMaxStatusComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        StatusComponentEntity status = dto.getStatusComponent().getEntity();
        buffer.writeIntLE(status.getMaxHp().intValue());
        buffer.writeIntLE(status.getMaxMp().intValue());
        buffer.writeIntLE(status.getMaxCp().intValue());
    }

}
