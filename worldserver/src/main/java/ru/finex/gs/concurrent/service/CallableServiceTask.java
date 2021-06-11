package ru.finex.gs.concurrent.service;

import org.hibernate.Session;
import ru.finex.core.concurrent.CallableServerTask;

import java.util.concurrent.Callable;

/**
 * @author m0nster.mind
 */
public class CallableServiceTask<T> extends CallableServerTask<T> implements ServiceTask {

    public CallableServiceTask(Callable<T> callable, Session session) {
        super(callable);
    }

}
