package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.model.entity.StatComponentEntity;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIStatComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        StatComponentEntity stat = dto.getStatComponent().getEntity();
        buffer.writeShortLE(40); // stat.getActiveWeaponItem() != null ? 40 : 20
        buffer.writeIntLE(stat.getPAtk());
        buffer.writeIntLE(stat.getAttackSpeed());
        buffer.writeIntLE(stat.getPDef());
        buffer.writeIntLE(stat.getEvasion());
        buffer.writeIntLE(stat.getAccuracy());
        buffer.writeIntLE(stat.getCriticalRate());
        buffer.writeIntLE(stat.getMAtk());
        buffer.writeIntLE(stat.getCastSpeed());
        buffer.writeIntLE(stat.getAttackSpeed()); // Seems like atk speed - 1
        buffer.writeIntLE(stat.getMagicEvasion());
        buffer.writeIntLE(stat.getMDef());
        buffer.writeIntLE(stat.getMagicAccuracy());
        buffer.writeIntLE(stat.getMagicCriticalRate());
    }

}
