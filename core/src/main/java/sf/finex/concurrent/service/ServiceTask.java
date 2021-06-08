package sf.finex.concurrent.service;

import sf.finex.concurrent.ServerTask;

/**
 * @author m0nster.mind
 */
public class ServiceTask extends ServerTask {

    public ServiceTask(Runnable runnable) {
        super(runnable);
    }

}
