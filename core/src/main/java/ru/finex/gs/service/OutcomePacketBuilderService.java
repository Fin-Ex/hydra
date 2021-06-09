package ru.finex.gs.service;

import ru.finex.gs.model.AuthFailReason;
import ru.finex.gs.model.GameObject;
import sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author m0nster.mind
 */
public interface OutcomePacketBuilderService {

    L2GameServerPacket keyPacket(byte[] key);
    L2GameServerPacket charSelectInfo(String login, int sessionId);
    L2GameServerPacket authLoginFail(AuthFailReason reason);
    L2GameServerPacket characterSelected(GameObject gameObject);
    L2GameServerPacket serverClose();
}
