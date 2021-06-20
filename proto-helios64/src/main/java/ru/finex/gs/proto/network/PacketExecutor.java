package ru.finex.gs.proto.network;

import ru.finex.gs.model.GameObject;
import ru.finex.gs.model.component.player.NetworkComponent;
import sf.l2j.commons.mmocore.IMMOExecutor;
import sf.l2j.commons.mmocore.ReceivablePacket;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class PacketExecutor implements IMMOExecutor<L2GameClient> {

    @Override
    public void execute(ReceivablePacket<L2GameClient> packet) {
        L2GameClient client = packet.getClient();
        GameObject gameObject = client.getGameObject();
        if (gameObject != null) {
            NetworkComponent networkComponent = gameObject.getComponent(NetworkComponent.class);
            networkComponent.addAction(packet);
        } else {
            client.getServicePacketExecutor().scheduleExecutePacket(packet);
        }
    }

}
