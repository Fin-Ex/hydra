package ru.finex.gs.concurrent.game;

import ru.finex.gs.model.Client;
import ru.finex.gs.model.GameObject;

/**
 * @author m0nster.mind
 */
public interface GameTask {

    Client getClient();
    GameObject getGameObject();

}
