package ru.finex.ws.hydra.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.hydra.model.entity.AvatarPrototypeView;
import ru.finex.ws.hydra.network.OutcomePacketBuilderService;
import ru.finex.ws.hydra.network.session.GameClient;
import ru.finex.ws.hydra.service.AvatarService;

import java.util.List;
import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class NewCharacterCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final GameClient session;

    private final OutcomePacketBuilderService packets;
    private final AvatarService avatarService;

    @Override
    public void executeCommand() {
        // TODO m0nster.mind: NewCharacterFail?

        List<AvatarPrototypeView> prototypes = avatarService.getPrototypes();
        session.sendPacket(packets.newCharacterSuccess(prototypes));
    }

}
