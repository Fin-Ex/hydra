package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.component.SpeedComponent;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIAnimationComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        SpeedComponent speedComponent = dto.getSpeedComponent();
        buffer.writeLongLE(Double.doubleToLongBits(speedComponent.getAnimMoveSpeed()));
        buffer.writeLongLE(Double.doubleToLongBits(speedComponent.getAnimAttackSpeed()));
    }

}
