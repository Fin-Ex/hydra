package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.network.model.dto.AuthKeyDto;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AuthClientCommand extends AbstractNetworkCommand {

    private final AuthKeyDto dto;
    private final GameClient session;

    @Override
    public void executeCommand() {
        if (session.getLogin() != null) {
            return;
        }

        String login = dto.getLogin();
        session.setLogin(login);
    }
}
