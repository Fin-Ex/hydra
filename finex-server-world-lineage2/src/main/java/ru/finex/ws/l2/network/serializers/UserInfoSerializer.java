package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.UserInfoComponent;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;
import ru.finex.ws.l2.network.serializers.userinfo.UIComponentSerializer;

import java.util.Arrays;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@OutcomePacket(@Opcode(0x32))
public class UserInfoSerializer implements PacketSerializer<UserInfoDto> {

    @Inject
    @Named("UIComponents")
    private Map<UserInfoComponent, UIComponentSerializer> serializers;

    @Override
    public void serialize(UserInfoDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getRuntimeId());

        int position = buffer.writerIndex();
        buffer.writeIntLE(0x00); // write it later
        buffer.writeShortLE(UserInfoComponent.count());
        SerializerHelper.writeReverseMediumLE(buffer, dto.flags());

        writeComponents(dto, buffer);

        // plus runtimeId & opcode
        int size = buffer.writerIndex() - position + 5;
        log.debug("UI Packet size: {}", size);
        buffer.setIntLE(position, size);
    }

    private void writeComponents(UserInfoDto dto, ByteBuf buffer) {
        UserInfoComponent[] components = dto.getComponents().toArray(new UserInfoComponent[0]);
        Arrays.sort(components);
        for (int i = 0; i < components.length; i++) {
            UserInfoComponent component = components[i];
            UIComponentSerializer serializer = serializers.get(component);
            if (serializer == null) {
                log.warn("Serializer for component: '{}' not found. {}", component, dto);
                continue;
            }

            int position = buffer.writerIndex();
            if (serializer.isSized()) {
                buffer.writeShortLE(0x00); // write it later
            }

            serializer.writeComponent(dto, buffer);

            if (serializer.isSized()) {
                int componentSize = buffer.writerIndex() - position;
                log.debug("UI[{}] Component size: {}", component.name(), componentSize);
                buffer.setShortLE(position, componentSize);
            }
        }
    }

}
