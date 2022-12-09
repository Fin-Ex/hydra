package ru.finex.auth.l2.command.network;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.auth.l2.model.FailReason;
import ru.finex.auth.l2.network.GameSession;
import ru.finex.auth.l2.network.OutcomePacketBuilderService;
import ru.finex.auth.l2.network.model.dto.RequestAuthLoginDto;
import ru.finex.auth.l2.service.SessionService;
import ru.finex.auth.model.AuthState;
import ru.finex.auth.model.exception.UserAlreadyAuthorizedException;
import ru.finex.auth.service.AuthService;
import ru.finex.auth.service.UserService;
import ru.finex.core.command.AbstractNetworkCommand;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class RequestAuthLoginCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final RequestAuthLoginDto dto;
    @ToString.Include
    private final GameSession session;

    private final AuthService authService;
    private final UserService userService;
    private final OutcomePacketBuilderService packets;
    private final SessionService sessionService;

    @Transactional
    @Override
    public void executeCommand() {
        if (session.getData().getSessionId() != dto.getSessionId()) {
            session.close(packets.loginFail(FailReason.REASON_ACCESS_FAILED));
            return;
        }

        session.setLogin(dto.getLogin());

        AuthState state = authorize();
        switch (state) {
            case AUTHED -> successAuthorize();
            default -> session.close(packets.loginFail(FailReason.REASON_ACCESS_FAILED));
        }
    }

    private void successAuthorize() {
        var user = userService.getByLogin(dto.getLogin());
        userService.refreshAuthDate(user.getPersistenceId());
        session.getData().setUserId(user.getPersistenceId());
        if (sessionService.authorizeSession(dto.getLogin(), session.getData())) {
            // check active world sessions
            session.sendPacket(packets.loginOk(session));
        } else {
            sessionService.revokeAuthorize(dto.getLogin());
            authService.logoutUser(dto.getLogin());
            session.close(packets.loginFail(FailReason.REASON_ACCOUNT_IN_USE));
        }
    }

    private AuthState authorize() {
        AuthState state;
        try {
            state = authService.authUser(dto.getLogin(), dto.getPassword());
        } catch (UserAlreadyAuthorizedException e) {
            // revoke world session
            sessionService.revokeAuthorize(dto.getLogin());
            authService.logoutUser(dto.getLogin());
            state = AuthState.NONE;
        }

        if (state == AuthState.CHECK_2FA) {
            state = authService.authUserTOTP(dto.getLogin(), Integer.toString(dto.getNcOtp()));
        }

        return state;
    }

}
