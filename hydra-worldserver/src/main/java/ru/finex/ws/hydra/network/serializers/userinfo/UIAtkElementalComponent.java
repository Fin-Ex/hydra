package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIAtkElementalComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
//        final AttributeType attackAttribute = _player.getAttackElement();
//        buffer.writeByte(attackAttribute.getClientId());
//        buffer.writeShortLE(_player.getAttackElementValue(attackAttribute));*/
        buffer.writeByte(0x00);
        buffer.writeShortLE(0x00);
    }

}
