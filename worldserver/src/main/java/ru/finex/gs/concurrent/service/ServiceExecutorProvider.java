package ru.finex.gs.concurrent.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ServiceExecutorProvider implements Provider<ScheduledExecutorService> {

    private final ScheduledExecutorService executorService;

    public ServiceExecutorProvider() {
        executorService = Executors.newScheduledThreadPool(
            4,
            new ThreadFactoryBuilder()
                .setNameFormat("GameThread-%d")
                .build()
        );
    }

    @Override
    public ScheduledExecutorService get() {
        return executorService;
    }
}
