package ru.finex.auth.hydra.network.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.auth.hydra.model.dto.UserServerDto;
import ru.finex.auth.hydra.network.model.dto.ServerListDto;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x04))
public class ServerListSerializer implements PacketSerializer<ServerListDto> {

    @Override
    public void serialize(ServerListDto dto, ByteBuf buffer) {
        var servers = dto.getServers();
        buffer.writeByte(servers.size());
        buffer.writeByte(dto.getLastServerId());
        for (UserServerDto server : servers) {
            buffer.writeByte(server.getId());
            buffer.writeBytes(server.getAddress().getAddress().getAddress());
            buffer.writeIntLE(server.getAddress().getPort());
            buffer.writeByte(server.getAgeLimit());
            buffer.writeByte(server.isPvp() ? 0x01 : 0x00);
            buffer.writeShortLE(server.getOnline());
            buffer.writeShortLE(server.getMaxClients());
            buffer.writeByte(0x01); // enabled / disabled server
            buffer.writeIntLE(buildServerSettings(server));
            buffer.writeByte(server.isBrackets() ? 0x01 : 0x00);
        }
        buffer.writeShortLE(0xa4); // ???
        for (UserServerDto server : servers) {
            buffer.writeByte(server.getId());
            buffer.writeByte(server.getRegisteredAvatars());
        }
    }

    private int buildServerSettings(UserServerDto server) {
        int settings = 0;
        if (server.isNormal()) {
            settings |= 1 << 0;
        }

        if (server.isRelax()) {
            settings |= 1 << 1;
        }

        if (server.isPublicTest()) {
            settings |= 1 << 2;
        }

        if (server.isNoLabel()) {
            settings |= 1 << 3;
        }

        if (server.isDeniedAvatarCreation()) {
            settings |= 1 << 4;
        }

        if (server.isEvent()) {
            settings |= 1 << 5;
        }

        if (server.isFree()) {
            settings |= 1 << 6;
        }

        return settings;
    }

}
