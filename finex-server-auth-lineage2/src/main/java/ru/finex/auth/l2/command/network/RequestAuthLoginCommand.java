package ru.finex.auth.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.model.LoginFailReason;
import ru.finex.auth.l2.network.GameSession;
import ru.finex.auth.l2.network.OutcomePacketBuilderService;
import ru.finex.auth.l2.network.model.dto.RequestAuthLoginDto;
import ru.finex.auth.model.AuthState;
import ru.finex.auth.service.AuthService;
import ru.finex.core.command.AbstractNetworkCommand;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class RequestAuthLoginCommand extends AbstractNetworkCommand {

    private final RequestAuthLoginDto dto;
    private final GameSession session;

    private final AuthService authService;
    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        AuthState state = authorize();

        switch (state) {
            case AUTHED -> session.sendPacket(packets.loginOk(session));
            default -> session.close(packets.loginFail(LoginFailReason.REASON_USER_OR_PASS_WRONG));
        }
    }

    private AuthState authorize() {
        AuthState state = authService.authUser(dto.getLogin(), dto.getPassword());
        if (state == AuthState.CHECK_2FA) {
            state = authService.authUserTOTP(dto.getLogin(), Integer.toString(dto.getNcOtp()));
        }

        return state;
    }

}
