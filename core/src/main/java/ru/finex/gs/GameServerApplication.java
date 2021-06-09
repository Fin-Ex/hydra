package ru.finex.gs;

import com.google.inject.Injector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.ApplicationBuilt;
import ru.finex.core.ServerApplication;
import ru.finex.core.ServerContext;
import sf.l2j.commons.lang.StringUtil;
import sf.l2j.commons.mmocore.SelectorThread;
import sf.l2j.gameserver.LoginServerThread;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class GameServerApplication implements ServerContext, ApplicationBuilt {

    @Getter @Setter
    private Injector injector;

    @Inject private LoginServerThread loginServerThread;
    @Inject private SelectorThread selectorThread;

    public static void main(String[] args) {
        StringUtil.printSection("Fin-Ex: Interlude");
        ServerApplication.start(GameServerApplication.class.getPackageName(), args);
    }

    @Override
    public void onApplicationBuilt() {
        loginServerThread.start();
        selectorThread.start();
    }

}
