package ru.finex.gs.concurrent.service;

import lombok.Getter;
import org.hibernate.Session;
import ru.finex.core.concurrent.RunnableServerTask;

/**
 * @author m0nster.mind
 */
public class RunnableServiceTask extends RunnableServerTask implements ServiceTask {

    @Getter
    private final Session dbSession;

    public RunnableServiceTask(Runnable runnable, Session session) {
        super(runnable);
        this.dbSession = session;
    }

}
