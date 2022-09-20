package ru.finex.auth.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.model.LoginFailReason;
import ru.finex.auth.l2.network.GameSession;
import ru.finex.auth.l2.network.OutcomePacketBuilderService;
import ru.finex.auth.l2.network.model.dto.RequestServerListDto;
import ru.finex.core.command.AbstractNetworkCommand;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class RequestServerListCommand extends AbstractNetworkCommand {

    private final RequestServerListDto dto;
    private final GameSession session;

    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        if (dto.getSessionKey() != session.getSessionKey()) {
            session.close(packets.loginFail(LoginFailReason.REASON_SYSTEM_ERROR));
            return;
        }

        session.sendPacket(null); // send ServerList
    }

}
