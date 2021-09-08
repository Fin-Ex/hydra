package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.GameObject;
import ru.finex.ws.l2.auth.model.AuthFailReason;
import ru.finex.ws.l2.avatar.AvatarService;
import ru.finex.ws.l2.component.base.CoordinateComponent;
import ru.finex.ws.l2.component.base.StatusComponent;
import ru.finex.ws.l2.component.player.AbnormalComponent;
import ru.finex.ws.l2.component.player.ClanComponent;
import ru.finex.ws.l2.component.player.ClassComponent;
import ru.finex.ws.l2.component.player.ClientComponent;
import ru.finex.ws.l2.component.player.CollisionComponent;
import ru.finex.ws.l2.component.player.CubicComponent;
import ru.finex.ws.l2.component.player.MountComponent;
import ru.finex.ws.l2.component.player.PlayerComponent;
import ru.finex.ws.l2.component.player.RecommendationComponent;
import ru.finex.ws.l2.component.player.SpeedComponent;
import ru.finex.ws.l2.component.player.StateComponent;
import ru.finex.ws.l2.component.player.StoreComponent;
import ru.finex.ws.l2.model.dto.SelectedAvatarDto;
import ru.finex.ws.l2.network.model.L2GameClient;
import ru.finex.ws.l2.network.model.L2GameServerPacket;
import ru.finex.ws.l2.network.outcome.AuthLoginFail;
import ru.finex.ws.l2.network.outcome.CharSelectInfo;
import ru.finex.ws.l2.network.outcome.CharacterSelected;
import ru.finex.ws.l2.network.outcome.KeyPacket;
import ru.finex.ws.l2.network.outcome.UserInfo;

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
    private final ComponentService componentService;

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
        ClientComponent clientComponent = componentService.getComponent(gameObject, ClientComponent.class);
        L2GameClient client = (L2GameClient) clientComponent.getClient();

        CharacterSelected packet = packetService.createOutcomePacket(0x15);
        packet.setRuntimeId(gameObject.getRuntimeId());
        packet.setSessionId(client.getSessionId().playOkID1);

        SelectedAvatarDto avatar = new SelectedAvatarDto();
        avatar.setPlayer(componentService.getComponent(gameObject, PlayerComponent.class).getEntity().clone());
        avatar.setClan(componentService.getComponent(gameObject, ClanComponent.class).getEntity().clone());
        avatar.setPosition(componentService.getComponent(gameObject, CoordinateComponent.class).getPosition().clone());
        avatar.setStatus(componentService.getComponent(gameObject, StatusComponent.class).getStatusEntity().clone());
        packet.setAvatar(avatar);

        return packet;
    }

    public L2GameServerPacket serverClose() {
        return packetService.createOutcomePacket(0x26);
    }
    
    public L2GameServerPacket castleManorList() {
        return packetService.createOutcomePacket(0xfe, 0x1b);
    }
    
    public L2GameServerPacket userInfo(GameObject gameObject) {
        UserInfo packet = packetService.createOutcomePacket(0x04);
        packet.setRuntimeId(gameObject.getRuntimeId());
        packet.setAbnormalComponent(componentService.getComponent(gameObject, AbnormalComponent.class));
        packet.setClanComponent(componentService.getComponent(gameObject, ClanComponent.class));
        packet.setClassComponent(componentService.getComponent(gameObject, ClassComponent.class));
        packet.setCollisionComponent(componentService.getComponent(gameObject, CollisionComponent.class));
        packet.setCoordinateComponent(componentService.getComponent(gameObject, CoordinateComponent.class));
        packet.setCubicComponent(componentService.getComponent(gameObject, CubicComponent.class));
        packet.setMountComponent(componentService.getComponent(gameObject, MountComponent.class));
        packet.setPlayerComponent(componentService.getComponent(gameObject, PlayerComponent.class));
        packet.setRecommendationComponent(componentService.getComponent(gameObject, RecommendationComponent.class));
        packet.setSpeedComponent(componentService.getComponent(gameObject, SpeedComponent.class));
        packet.setStateComponent(componentService.getComponent(gameObject, StateComponent.class));
        packet.setStatusComponent(componentService.getComponent(gameObject, StatusComponent.class));
        packet.setStoreComponent(componentService.getComponent(gameObject, StoreComponent.class));
        return packet;
    }
}
