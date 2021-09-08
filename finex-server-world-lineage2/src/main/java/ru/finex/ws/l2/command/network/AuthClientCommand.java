package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.ws.l2.network.AbstractNetworkCommand;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.l2.network.model.dto.AuthKeyDto;
import sf.l2j.commons.crypt.SessionKey;
import sf.l2j.gameserver.LoginServerThread;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class AuthClientCommand extends AbstractNetworkCommand {

    private final AuthKeyDto dto;

    @Inject
    private LoginServerThread loginServerThread;

    @Override
    public void executeCommand() {
        L2GameClient client = (L2GameClient) getClient();
        if (client.getLogin() != null) {
            return;
        }

        String login = dto.getLogin();
        client.setLogin(login);
        client.setSessionId(new SessionKey(
            dto.getLoginKey1(),
            dto.getLoginKey2(),
            dto.getPlayKey1(),
            dto.getPlayKey2()
        ));

        loginServerThread.addClient(login, client);
    }
}
