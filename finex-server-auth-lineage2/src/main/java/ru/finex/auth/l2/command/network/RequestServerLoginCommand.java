package ru.finex.auth.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.model.LoginFailReason;
import ru.finex.auth.l2.network.GameSession;
import ru.finex.auth.l2.network.OutcomePacketBuilderService;
import ru.finex.auth.l2.network.model.dto.RequestServerLoginDto;
import ru.finex.core.command.AbstractNetworkCommand;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class RequestServerLoginCommand extends AbstractNetworkCommand {

    private final RequestServerLoginDto dto;
    private final GameSession session;

    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        if (dto.getSessionKey() != session.getSessionKey()) {
            session.close(packets.loginFail(LoginFailReason.REASON_SYSTEM_ERROR));
            return;
        }

        // check GS available
        session.sendPacket(null); // send PlayOk
    }

}
