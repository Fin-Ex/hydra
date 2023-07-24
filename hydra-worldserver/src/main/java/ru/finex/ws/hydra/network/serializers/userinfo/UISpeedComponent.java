package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.component.SpeedComponent;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UISpeedComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        SpeedComponent speedComponent = dto.getSpeedComponent();
        buffer.writeShortLE((int) speedComponent.getRunSpeed());
        buffer.writeShortLE((int) speedComponent.getWalkSpeed());
        buffer.writeShortLE((int) speedComponent.getSwimSpeed());
        buffer.writeShortLE((int) speedComponent.getSwimSpeed());
        buffer.writeShortLE((int) speedComponent.getFlySpeed());
        buffer.writeShortLE((int) speedComponent.getFlySpeed());
        buffer.writeShortLE((int) speedComponent.getFlySpeed());
        buffer.writeShortLE((int) speedComponent.getFlySpeed());
    }

}
