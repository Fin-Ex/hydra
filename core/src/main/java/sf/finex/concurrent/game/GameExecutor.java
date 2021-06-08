package sf.finex.concurrent.game;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @author m0nster.mind
 */
public class GameExecutor extends ScheduledThreadPoolExecutor {
    public GameExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public GameExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public GameExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public GameExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        GameThread thread = (GameThread) t;
        GameTask task = (GameTask) r;
        thread.setClient(task.getClient());
        thread.setPlayer(task.getPlayer());
    }
}
