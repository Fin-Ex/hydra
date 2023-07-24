package ru.finex.auth.hydra.service;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.NetworkCommandQueue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class CommandExecutorService {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final NetworkCommandQueue queue;

    @PostConstruct
    private void beginProcessing() {
        executor.scheduleAtFixedRate(() -> queue.executeCommands(), 200, 200, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    private void shutdown() {
        executor.shutdown();
        try {
            executor.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

}
