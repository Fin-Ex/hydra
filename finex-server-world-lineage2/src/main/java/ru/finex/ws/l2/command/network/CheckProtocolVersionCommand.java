package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.ProtocolVersionDto;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.service.WorldCodecService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class CheckProtocolVersionCommand extends AbstractNetworkCommand {

    private static final int PV_PING = -2;
    private static final int PV_STATUS = -3;
    private static final int PV_VALID_CLIENT = 64;

    @ToString.Include
    private final ProtocolVersionDto dto;
    @ToString.Include
    private final GameClient session;

    private final OutcomePacketBuilderService packets;
    private final WorldCodecService codecService;

    @Override
    public void executeCommand() {
        int protocolVersion = dto.getVersion();
        if (protocolVersion != PV_VALID_CLIENT) {
            NetworkDto packet;
            if (dto.getVersion() == PV_PING) {
                packet = null;
                return;
            } else if (dto.getVersion() == PV_STATUS) {
                packet = null; // TODO: m0nster.mind SendStatus packet
                return;
            } else {
                packet = packets.versionCheck(codecService.getEmptyKey(), false);
            }

            session.close(packet);
            return;
        }

        byte[] key = codecService.getBlowfishKey();
        session.setCryptKey(key);
        session.sendPacket(packets.versionCheck(key, true));
    }

}
