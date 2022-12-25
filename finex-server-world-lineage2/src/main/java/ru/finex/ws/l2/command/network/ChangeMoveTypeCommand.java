package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.object.GameObject;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.ChangeMoveTypeDto;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.service.component.StateService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ChangeMoveTypeCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final ChangeMoveTypeDto dto;
    @ToString.Include
    private final GameClient session;

    private final StateService stateService;
    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        GameObject gameObject = session.getGameObject();
        stateService.setRunning(gameObject, dto.isRunning());

        // FIXME m0nster.mind: это должно быть не тут, пакет отправляется в конце тика,
        //  если у компонента установлен грязный флаг
        session.sendPacket(packets.changeMoveType(gameObject));
    }

}
