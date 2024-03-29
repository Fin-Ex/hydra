package ru.finex.ws.hydra.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.hydra.model.entity.ActiveClassComponentEntity;
import ru.finex.ws.hydra.model.entity.StatusComponentEntity;
import ru.finex.ws.hydra.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIStatusComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        StatusComponentEntity status = dto.getStatusComponent().getEntity();
        ActiveClassComponentEntity activeClass = dto.getClassComponent().getEntity();

        buffer.writeIntLE((int) Math.round(status.getHp()));
        buffer.writeIntLE((int) Math.round(status.getMp()));
        buffer.writeIntLE((int) Math.round(status.getCp()));
        buffer.writeLongLE(activeClass.getSp());
        buffer.writeLongLE(activeClass.getExp());
        buffer.writeLongLE(Double.doubleToLongBits(0f)); // (float) (_player.getExp() - ExperienceData.getInstance().getExpForLevel(_player.getLevel())) / (ExperienceData.getInstance().getExpForLevel(_player.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(_player.getLevel()))
    }

}
