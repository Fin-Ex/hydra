package sf.finex.service.concurrent;

import sf.finex.concurrent.game.GameTask;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author m0nster.mind
 */
public interface GameExecutorService {

    Future<?> execute(GameTask task);
    Future<?> execute(GameTask task, long delay, TimeUnit delayUnit);
    ScheduledFuture<?> execute(GameTask task, long delay, long period, TimeUnit timeUnit);

}
