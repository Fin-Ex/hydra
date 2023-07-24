package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.component.ColliderComponent;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIColliderComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        ColliderComponent collisionComponent = dto.getCollisionComponent();
        buffer.writeLongLE(Double.doubleToLongBits(collisionComponent.getWidth()));
        buffer.writeLongLE(Double.doubleToLongBits(collisionComponent.getHeight()));
    }

}
