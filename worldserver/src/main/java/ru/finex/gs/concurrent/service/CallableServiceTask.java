package ru.finex.gs.concurrent.service;

import lombok.Getter;
import org.hibernate.Session;
import ru.finex.core.concurrent.CallableServerTask;

import java.util.concurrent.Callable;

/**
 * @author m0nster.mind
 */
public class CallableServiceTask<T> extends CallableServerTask<T> implements ServiceTask {

    @Getter
    private final Session dbSession;

    public CallableServiceTask(Callable<T> callable, Session session) {
        super(callable);
        this.dbSession = session;
    }

}
