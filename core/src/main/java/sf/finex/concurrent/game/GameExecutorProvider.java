package sf.finex.concurrent.game;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class GameExecutorProvider implements Provider<ScheduledExecutorService> {

    private ScheduledExecutorService executorService;

    public GameExecutorProvider() {
        executorService = new GameExecutor(
            4,
            new ThreadFactoryBuilder()
                .setNameFormat("GameThread-%d")
                .setThreadFactory(GameThread::new)
                .build()
        );
    }

    @Override
    public ScheduledExecutorService get() {
        return executorService;
    }
}
