package ru.finex.gs.concurrent.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.finex.gs.model.Client;
import ru.finex.gs.model.GameObject;

/**
 * @author m0nster.mind
 */
public class GameThread extends Thread {

    @Getter @Setter(AccessLevel.PROTECTED)
    private Client client;

    @Getter @Setter(AccessLevel.PROTECTED)
    private GameObject gameObject;

    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain #Thread(ThreadGroup,Runnable,String) Thread}
     * {@code (null, null, gname)}, where {@code gname} is a newly generated
     * name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     */
    public GameThread() {
        super();
    }

    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain #Thread(ThreadGroup,Runnable,String) Thread}
     * {@code (null, target, gname)}, where {@code gname} is a newly generated
     * name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     *
     * @param  target
     *         the object whose {@code run} method is invoked when this thread
     *         is started. If {@code null}, this classes {@code run} method does
     *         nothing.
     */
    public GameThread(Runnable target) {
        super(target);
    }

}