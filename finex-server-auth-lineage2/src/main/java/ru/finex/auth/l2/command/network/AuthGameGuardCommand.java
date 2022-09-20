package ru.finex.auth.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.model.LoginFailReason;
import ru.finex.auth.l2.network.GameSession;
import ru.finex.auth.l2.network.OutcomePacketBuilderService;
import ru.finex.auth.l2.network.model.dto.AuthGameGuardDto;
import ru.finex.core.command.AbstractNetworkCommand;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AuthGameGuardCommand extends AbstractNetworkCommand {

    private final AuthGameGuardDto dto;
    private final GameSession session;
    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        if (session.getSessionId() != dto.getSessionId()) {
            session.close(packets.loginFail(LoginFailReason.REASON_SYSTEM_ERROR));
        }

        session.sendPacket(packets.ggAuth(session));
    }

}
