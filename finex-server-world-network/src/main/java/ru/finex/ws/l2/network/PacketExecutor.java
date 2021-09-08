package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.model.GameObject;
import ru.finex.gs.command.InputCommandService;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.l2.network.model.L2GameClientPacket;
import ru.finex.ws.l2.network.model.NetworkDto;
import sf.l2j.commons.mmocore.IMMOExecutor;
import sf.l2j.commons.mmocore.ReceivablePacket;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PacketExecutor implements IMMOExecutor<L2GameClient> {

    private final InputCommandService inputCommandService;
    private final NetworkCommandRegistry commandRegistry;

    @Override
    public void execute(ReceivablePacket<L2GameClient> packet) {
        L2GameClient client = packet.getClient();
        GameObject gameObject = client.getGameObject();

        L2GameClientPacket gsPacket = (L2GameClientPacket) packet;
        NetworkDto dto = gsPacket.getDto();

        AbstractNetworkCommand command;
        try {
            command = commandRegistry.createCommand(gsPacket, dto);
        } catch (RuntimeException e) {
            log.error("Fail to create network command for packet: {}", gsPacket, e);
            return;
        }

        if (command == null) {
            return; // no operation
        }

        command.setClient(client);
        command.setGameObject(gameObject);
        inputCommandService.offerCommand(command);
    }

}
