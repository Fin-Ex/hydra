package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.component.ColliderComponent;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

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
