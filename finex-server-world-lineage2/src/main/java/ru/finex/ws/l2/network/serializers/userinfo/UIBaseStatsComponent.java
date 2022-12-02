package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.model.entity.ParameterComponentEntity;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIBaseStatsComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        ParameterComponentEntity parameters = dto.getParameterComponent().getEntity();
        buffer.writeShortLE(parameters.getSTR());
        buffer.writeShortLE(parameters.getDEX());
        buffer.writeShortLE(parameters.getCON());
        buffer.writeShortLE(parameters.getINT());
        buffer.writeShortLE(parameters.getWIT());
        buffer.writeShortLE(parameters.getMEN());
        buffer.writeShortLE(parameters.getLUC());
        buffer.writeShortLE(parameters.getCHA());
    }

}
