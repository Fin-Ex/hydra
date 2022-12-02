package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.command.network.CharacterCreateCommand;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.dto.CharCreateOk;
import ru.finex.ws.l2.network.model.dto.CharacterCreateDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(value = {@Opcode(0x0F)})
public class CharCreateOkSerializer implements PacketSerializer<CharCreateOk> {

    @Override
    public void serialize(CharCreateOk dto, ByteBuf buffer) {
        buffer.writeIntLE(0x01);
    }
}
