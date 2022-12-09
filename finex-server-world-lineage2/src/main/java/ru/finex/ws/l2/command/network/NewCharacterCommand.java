package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.l2.model.entity.AvatarPrototypeView;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.service.AvatarService;

import java.util.List;
import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class NewCharacterCommand extends AbstractNetworkCommand {

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
