package ru.finex.gs.proto.interlude.income;

import ru.finex.gs.proto.network.IncomePacket;
import ru.finex.gs.proto.network.L2GameClient;
import ru.finex.gs.proto.network.L2GameClientPacket;
import ru.finex.gs.proto.network.Opcode;
import sf.l2j.commons.crypt.SessionKey;
import sf.l2j.gameserver.LoginServerThread;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@IncomePacket(@Opcode(0x08))
public class AuthKey extends L2GameClientPacket {

    private String login;
    private int playKey1;
    private int playKey2;
    private int loginKey1;
    private int loginKey2;

    @Inject
    private LoginServerThread loginServerThread;

    @Override
    protected void readImpl() {
        login = readS().toLowerCase();
        playKey2 = readD();
        playKey1 = readD();
        loginKey1 = readD();
        loginKey2 = readD();
    }

    @Override
    protected void runImpl() {
        L2GameClient client = getClient();
        if (client.getLogin() != null) {
            return;
        }

        client.setLogin(login);
        client.setSessionId(new SessionKey(loginKey1, loginKey2, playKey1, playKey2));

        loginServerThread.addClient(login, client);
    }
}
