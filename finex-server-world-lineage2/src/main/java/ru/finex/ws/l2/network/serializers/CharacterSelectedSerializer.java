package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.model.entity.ClanEntity;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.ws.l2.model.entity.PositionEntity;
import ru.finex.ws.l2.model.entity.StatusEntity;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.dto.CharacterSelectedDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x0b))
public class CharacterSelectedSerializer implements PacketSerializer<CharacterSelectedDto> {

    @Override
    public void serialize(CharacterSelectedDto dto, ByteBuf buffer) {
        var avatar = dto.getAvatar();
        PlayerEntity player = avatar.getPlayer();
        ClanEntity clan = avatar.getClan();
        PositionEntity position = avatar.getPosition();
        StatusEntity status = avatar.getStatus();

        SerializerHelper.writeStringNullTerm(buffer, player.getName());
        buffer.writeIntLE(dto.getRuntimeId());
        SerializerHelper.writeStringNullTerm(buffer, player.getTitle());
        buffer.writeIntLE(dto.getSessionId());
        buffer.writeIntLE(clan.getPersistenceId());
        buffer.writeIntLE(0x00); // FIXME m0nster.mind: access level
        buffer.writeIntLE(player.getGender().ordinal());
        buffer.writeIntLE(player.getRace().ordinal());
        buffer.writeIntLE(player.getAppearanceClass().getNetworkId(player.getRace()));
        buffer.writeIntLE(0x01); // selected
        buffer.writeIntLE((int)position.getX());
        buffer.writeIntLE((int)position.getY());
        buffer.writeIntLE((int)position.getZ());
        buffer.writeLongLE(Double.doubleToLongBits(status.getHp()));
        buffer.writeLongLE(Double.doubleToLongBits(status.getMp()));
        buffer.writeLongLE(0); // sp
        buffer.writeLongLE(0); // exp
        buffer.writeIntLE(1); // level
        buffer.writeIntLE(0); // karma
        buffer.writeIntLE(0); // pk count
        buffer.writeIntLE(0); // game time
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(ClassId.HumanFighter.ordinal());
        buffer.writeBytes(new byte[16]);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(0x00);
        buffer.writeBytes(new byte[28]);
        buffer.writeIntLE(0x00);
    }

}
