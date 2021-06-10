package ru.finex.gs.concurrent.service;

import ru.finex.gs.concurrent.ServerTask;

/**
 * @author m0nster.mind
 */
public class ServiceTask extends ServerTask {

    public ServiceTask(Runnable runnable) {
        super(runnable);
    }

}
