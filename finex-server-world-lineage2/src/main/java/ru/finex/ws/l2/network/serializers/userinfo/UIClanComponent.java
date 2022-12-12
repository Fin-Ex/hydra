package ru.finex.ws.l2.network.serializers.userinfo;

import io.netty.buffer.ByteBuf;
import ru.finex.ws.l2.model.entity.ClanComponentEntity;
import ru.finex.ws.l2.model.entity.PlayerComponentEntity;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class UIClanComponent implements UIComponentSerializer {

    @Override
    public void writeComponent(UserInfoDto dto, ByteBuf buffer) {
        PlayerComponentEntity player = dto.getPlayerComponent().getEntity();
        ClanComponentEntity clan = dto.getClanComponent().getEntity();

        SerializerHelper.writeString(buffer, player.getTitle());
        buffer.writeShortLE(0x00); // pledge type
        buffer.writeIntLE(clan.getPersistenceId()); // clanId
        buffer.writeIntLE(clan.getPersistenceId()); // large crest id
        buffer.writeIntLE(clan.getPersistenceId()); // crest id
        buffer.writeIntLE(0x00); //FIXME finfan: clan.getClanPrivileges().getBitmask()
        buffer.writeByte(0x00); // FIXME finfan: isCLanLeader player.isClanLeader() ? 0x01 : 0x00
        buffer.writeIntLE(0x00); // FIXME finfan: ally id
        buffer.writeIntLE(0x00); // FIXME finfan: ally crest id
        buffer.writeByte(0x00); // FIXME finfan: clan.isInMatchingRoom() ? 0x01 : 0x00
    }

}
