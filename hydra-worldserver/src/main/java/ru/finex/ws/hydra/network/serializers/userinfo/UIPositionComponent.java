package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.model.entity.PositionComponentEntity;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIPositionComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        PositionComponentEntity position = dto.getCoordinateComponent().getEntity();
        buffer.writeIntLE(position.getX().intValue());
        buffer.writeIntLE(position.getY().intValue());
        buffer.writeIntLE(position.getZ().intValue());
        buffer.writeIntLE(0); //_player.isInVehicle() ? _player.getVehicle().getObjectId() : 0
    }

}
