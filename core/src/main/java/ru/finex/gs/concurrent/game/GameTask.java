package ru.finex.gs.concurrent.game;

import lombok.Getter;
import ru.finex.gs.concurrent.ServerTask;
import ru.finex.gs.model.GameObject;
import sf.l2j.gameserver.network.L2GameClient;

/**
 * Игровая задача.
 *
 * @author m0nster.mind
 */
public class GameTask extends ServerTask implements Runnable {

    @Getter
    private final L2GameClient client;

    @Getter
    private final GameObject gameObject;

    /**
     * Создать игровую задачу.
     * @param runnable задача
     * @param client клиент, который выполняет данную задачу
     */
    public GameTask(Runnable runnable, L2GameClient client) {
        this(runnable, client, null);
    }

    /**
     * Создать игровую задачу.
     * @param runnable задача
     * @param client клиент, который выполняет данную задачу
     * @param gameObject игрок, который выполняет данную задачу (can be null)
     */
    public GameTask(Runnable runnable, L2GameClient client, GameObject gameObject) {
        super(runnable);
        this.client = client;
        if (gameObject == null) {
            this.gameObject = client.getGameObject();
        } else {
            this.gameObject = gameObject;
        }
    }

}
