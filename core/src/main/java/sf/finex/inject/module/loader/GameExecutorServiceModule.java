package sf.finex.inject.module.loader;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import sf.finex.concurrent.game.GameExecutorProvider;
import sf.finex.concurrent.game.GameExecutorServiceImpl;
import sf.finex.inject.LoaderModule;
import sf.finex.service.concurrent.GameExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class GameExecutorServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class).annotatedWith(Names.named("Game")).toProvider(GameExecutorProvider.class);
        bind(ExecutorService.class).annotatedWith(Names.named("Game")).toProvider(GameExecutorProvider.class);
        bind(GameExecutorService.class).to(GameExecutorServiceImpl.class);
    }

}
