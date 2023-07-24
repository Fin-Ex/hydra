package ru.finex.ws.hydra.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.hydra.model.AuthFailReason;
import ru.finex.ws.hydra.model.exception.InvalidSessionException;
import ru.finex.ws.hydra.network.OutcomePacketBuilderService;
import ru.finex.ws.hydra.network.model.dto.AuthKeyDto;
import ru.finex.ws.hydra.network.session.GameClient;
import ru.finex.ws.hydra.network.session.GameClientState;
import ru.finex.ws.hydra.service.SessionService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AuthClientCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final AuthKeyDto dto;
    @ToString.Include
    private final GameClient session;

    private final OutcomePacketBuilderService packets;
    private final SessionService sessionService;

    @Override
    public void executeCommand() {
        int sessionId;
        try {
            sessionId = sessionService.authorizeSession(dto);
        } catch (InvalidSessionException e) {
            session.close(null);
            return;
        }

        String login = dto.getLogin();
        session.setLogin(login);
        session.setState(GameClientState.AUTHED);
        session.sendPacket(packets.authLoginFail(AuthFailReason.LOGIN_SUCCESS));
        session.sendPacket(packets.charSelectInfo(
            login,
            sessionId
        ));
    }
}
