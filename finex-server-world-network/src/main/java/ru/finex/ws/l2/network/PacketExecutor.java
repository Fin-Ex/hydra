package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.model.GameObject;
import ru.finex.ws.command.InputCommandService;
import ru.finex.ws.l2.network.model.NetworkDto;
import ru.finex.ws.model.ClientSession;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PacketExecutor {

    private final InputCommandService inputCommandService;
    private final NetworkCommandRegistry commandRegistry;

    @Override
    public void execute(ClientSession session, NetworkDto dto) {
        GameObject gameObject = session.getGameObject();

        AbstractNetworkCommand command;
        try {
            command = commandRegistry.createCommand(commandType, dto);
        } catch (RuntimeException e) {
            log.error("Fail to create network command for packet: {}", gsPacket, e);
            return;
        }

        if (command == null) {
            return; // no operation
        }

        command.setClient(session);
        command.setGameObject(gameObject);
        inputCommandService.offerCommand(command);
    }

}
