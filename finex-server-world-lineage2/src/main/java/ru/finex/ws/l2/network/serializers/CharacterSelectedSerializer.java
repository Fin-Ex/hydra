package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.model.enums.ClassId;
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
        SerializerHelper.writeStringNullTerm(buffer, dto.getName());
        buffer.writeIntLE(dto.getRuntimeId());
        SerializerHelper.writeStringNullTerm(buffer, dto.getTitle());
        buffer.writeIntLE(dto.getSessionId());
        buffer.writeIntLE(dto.getClanId());
        buffer.writeIntLE(0x00); // FIXME m0nster.mind: access level
        buffer.writeIntLE(dto.getGender().ordinal());
        buffer.writeIntLE(dto.getRace().ordinal());
        buffer.writeIntLE(dto.getAppearanceClass().getNetworkId(dto.getRace(), dto.getGender()));
        buffer.writeIntLE(0x01); // selected
        buffer.writeIntLE((int) dto.getX());
        buffer.writeIntLE((int) dto.getY());
        buffer.writeIntLE((int) dto.getZ());
        buffer.writeLongLE(Double.doubleToLongBits(dto.getHp()));
        buffer.writeLongLE(Double.doubleToLongBits(dto.getMp()));
        buffer.writeLongLE(0); // sp
        buffer.writeLongLE(0); // exp
        buffer.writeIntLE(1); // level
        buffer.writeIntLE(0); // karma
        buffer.writeIntLE(0); // pk count
        buffer.writeIntLE(0); // game time
        buffer.writeIntLE(0x00);
        buffer.writeIntLE(ClassId.HUMAN_FIGHTER.ordinal());
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
        buffer.skipBytes(28);
        buffer.writeIntLE(0x00);
    }

}
