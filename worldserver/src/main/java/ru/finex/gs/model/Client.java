package ru.finex.gs.model;

import ru.finex.nif.NetworkClient;
import sf.l2j.commons.crypt.SessionKey;

/**
 * @author m0nster.mind
 */
public interface Client extends NetworkClient {

    String getLogin();
    void setLogin(String login);

    SessionKey getSessionId();

    boolean isDetached();

    GameObject getGameObject();
    void setGameObject(GameObject gameObject);

    void closeNow();

}
