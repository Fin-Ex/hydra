package ru.finex.ws.l2.auth;

import ru.finex.core.ApplicationBuilt;
import sf.l2j.gameserver.LoginServerThread;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class L2JAuth implements ApplicationBuilt {

    @Inject
    private LoginServerThread loginServerThread;

    @Override
    public void onApplicationBuilt() {
        loginServerThread.start();
    }

}
