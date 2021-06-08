package sf.finex.concurrent.game;

import lombok.Getter;
import sf.finex.concurrent.ServerTask;
import sf.l2j.gameserver.model.actor.Player;
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
    private final Player player;

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
     * @param player игрок, который выполняет данную задачу (can be null)
     */
    public GameTask(Runnable runnable, L2GameClient client, Player player) {
        super(runnable);
        this.client = client;
        if (player == null) {
            this.player = client.getActiveChar();
        } else {
            this.player = player;
        }
    }

}
