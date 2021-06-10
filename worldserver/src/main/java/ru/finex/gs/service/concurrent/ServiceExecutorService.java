package ru.finex.gs.service.concurrent;

import ru.finex.gs.concurrent.service.ServiceTask;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author m0nster.mind
 */
public interface ServiceExecutorService {

    Future<?> execute(ServiceTask task);
    Future<?> execute(ServiceTask task, long delay, TimeUnit delayUnit);
    ScheduledFuture<?> execute(ServiceTask task, long delay, long period, TimeUnit timeUnit);

}
