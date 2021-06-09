package ru.finex.gs.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import sf.l2j.gameserver.network.L2GameClient;

/**
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
public class ClientDisconnected {

    private final L2GameClient client;

}
