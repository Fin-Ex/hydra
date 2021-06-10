package ru.finex.gs.concurrent;

import lombok.RequiredArgsConstructor;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public abstract class ServerTask implements Runnable {

    private final Runnable runnable;

    @Override
    public void run() {
        runnable.run();
    }

}
