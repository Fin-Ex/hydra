package ru.finex.gs.service;

import sf.l2j.gameserver.network.L2GameClient;

import java.util.List;

/**
 * @author m0nster.mind
 */
public interface ClientService {

    void addSession(L2GameClient client);
    void removeSession(L2GameClient client);

    /**
     * Возвращает копию списка подключенных клиентов.
     * @return список подключенных клиентов
     */
    List<L2GameClient> getSessions();

}
