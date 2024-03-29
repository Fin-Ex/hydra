package ru.finex.auth.hydra.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.auth.hydra.model.FailReason;
import ru.finex.auth.hydra.network.GameSession;
import ru.finex.auth.hydra.network.OutcomePacketBuilderService;
import ru.finex.auth.hydra.network.model.dto.AuthGameGuardDto;
import ru.finex.core.command.AbstractNetworkCommand;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AuthGameGuardCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final AuthGameGuardDto dto;
    @ToString.Include
    private final GameSession session;

    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        if (session.getData().getSessionId() != dto.getSessionId()) {
            session.close(packets.loginFail(FailReason.REASON_SYSTEM_ERROR));
        }

        session.sendPacket(packets.ggAuth(session));
    }

}
