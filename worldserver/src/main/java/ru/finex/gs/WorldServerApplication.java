package ru.finex.gs;

import com.google.inject.Injector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.ApplicationBuilt;
import ru.finex.core.ServerApplication;
import ru.finex.core.ServerContext;
import ru.finex.nif.SelectorThread;
import sf.l2j.gameserver.LoginServerThread;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class WorldServerApplication implements ServerContext, ApplicationBuilt {

    @Getter @Setter
    private Injector injector;

    @Inject private LoginServerThread loginServerThread;
    @Inject private SelectorThread selectorThread;

    public static void main(String[] args) {
        ServerApplication.start(WorldServerApplication.class.getPackageName(), args);
    }

    @Override
    public void onApplicationBuilt() {
        loginServerThread.start();
        selectorThread.start();
    }

}
