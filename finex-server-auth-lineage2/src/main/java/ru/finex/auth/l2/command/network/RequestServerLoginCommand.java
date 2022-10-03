package ru.finex.auth.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.auth.l2.model.FailReason;
import ru.finex.auth.l2.network.GameSession;
import ru.finex.auth.l2.network.OutcomePacketBuilderService;
import ru.finex.auth.l2.network.model.dto.RequestServerLoginDto;
import ru.finex.core.command.AbstractNetworkCommand;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class RequestServerLoginCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final RequestServerLoginDto dto;
    @ToString.Include
    private final GameSession session;

    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        if (dto.getSessionKey() != session.getData().getSessionKey()) {
            session.close(packets.loginFail(FailReason.REASON_SYSTEM_ERROR));
            return;
        }

        // TODO: m0nster.mind check GS available

        session.sendPacket(packets.playOk(session));
    }

}
