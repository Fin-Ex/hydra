package ru.finex.gs.proto.interlude;

import lombok.RequiredArgsConstructor;
import ru.finex.gs.model.AuthFailReason;
import ru.finex.gs.model.GameObject;
import ru.finex.gs.model.component.base.CoordinateComponent;
import ru.finex.gs.model.component.base.StatusComponent;
import ru.finex.gs.model.component.player.ClanComponent;
import ru.finex.gs.model.component.player.ClientComponent;
import ru.finex.gs.model.component.player.PlayerComponent;
import ru.finex.gs.model.dto.SelectedAvatarDto;
import ru.finex.gs.proto.interlude.outcome.AuthLoginFail;
import ru.finex.gs.proto.interlude.outcome.CharSelectInfo;
import ru.finex.gs.proto.interlude.outcome.CharacterSelected;
import ru.finex.gs.proto.interlude.outcome.KeyPacket;
import ru.finex.gs.proto.network.L2GameClient;
import ru.finex.gs.proto.network.L2GameServerPacket;
import ru.finex.gs.proto.network.PacketService;
import ru.finex.gs.service.AvatarService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class OutcomePacketBuilderService {

    private final PacketService packetService;
    private final AvatarService avatarService;

    public L2GameServerPacket keyPacket(byte[] key) {
        KeyPacket keyPacket = packetService.createOutcomePacket(0x00);
        keyPacket.setKey(key);
        return keyPacket;
    }

    public L2GameServerPacket charSelectInfo(String login, int sessionId) {
        CharSelectInfo packet = packetService.createOutcomePacket(0x13);
        packet.setLogin(login);
        packet.setSessionId(sessionId);
        packet.setAvatars(avatarService.getAvatars(login));
        return packet;
    }

    public L2GameServerPacket authLoginFail(AuthFailReason reason) {
        AuthLoginFail packet = packetService.createOutcomePacket(0x14);
        packet.setReason(reason);
        return packet;
    }

    public L2GameServerPacket characterSelected(GameObject gameObject) {
        ClientComponent clientComponent = gameObject.getComponent(ClientComponent.class);
        L2GameClient client = (L2GameClient) clientComponent.getClient();

        CharacterSelected packet = packetService.createOutcomePacket(0x15);
        packet.setRuntimeId(gameObject.getRuntimeId());
        packet.setSessionId(client.getSessionId().playOkID1);

        SelectedAvatarDto avatar = new SelectedAvatarDto();
        avatar.setPlayer(gameObject.getComponent(PlayerComponent.class).getEntity().clone());
        avatar.setClan(gameObject.getComponent(ClanComponent.class).getEntity().clone());
        avatar.setPosition(gameObject.getComponent(CoordinateComponent.class).getPosition().clone());
        avatar.setStatus(gameObject.getComponent(StatusComponent.class).getStatusEntity().clone());
        packet.setAvatar(avatar);

        return packet;
    }

    public L2GameServerPacket serverClose() {
        return packetService.createOutcomePacket(0x26);
    }
}
