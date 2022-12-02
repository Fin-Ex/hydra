package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.model.entity.StatusComponentEntity;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIStatusComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        StatusComponentEntity status = dto.getStatusComponent().getStatusEntity();
        buffer.writeIntLE((int) Math.round(status.getHp()));
        buffer.writeIntLE((int) Math.round(status.getMp()));
        buffer.writeIntLE((int) Math.round(status.getCp()));
        buffer.writeLongLE(0L); // sp
        buffer.writeLongLE(0L); // exp
        buffer.writeLongLE(Double.doubleToLongBits(0f)); // (float) (_player.getExp() - ExperienceData.getInstance().getExpForLevel(_player.getLevel())) / (ExperienceData.getInstance().getExpForLevel(_player.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(_player.getLevel()))
    }

}
