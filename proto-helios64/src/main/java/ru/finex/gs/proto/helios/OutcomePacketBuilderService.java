package ru.finex.gs.proto.helios;

import lombok.RequiredArgsConstructor;
import ru.finex.gs.model.AuthFailReason;
import ru.finex.gs.model.GameObject;
import ru.finex.gs.model.component.base.CoordinateComponent;
import ru.finex.gs.model.component.base.StatusComponent;
import ru.finex.gs.model.component.player.AbnormalComponent;
import ru.finex.gs.model.component.player.ClanComponent;
import ru.finex.gs.model.component.player.ClassComponent;
import ru.finex.gs.model.component.player.ClientComponent;
import ru.finex.gs.model.component.player.CollisionComponent;
import ru.finex.gs.model.component.player.CubicComponent;
import ru.finex.gs.model.component.player.MountComponent;
import ru.finex.gs.model.component.player.PlayerComponent;
import ru.finex.gs.model.component.player.RecommendationComponent;
import ru.finex.gs.model.component.player.SpeedComponent;
import ru.finex.gs.model.component.player.StateComponent;
import ru.finex.gs.model.component.player.StoreComponent;
import ru.finex.gs.model.dto.SelectedAvatarDto;
import ru.finex.gs.proto.helios.outcome.AuthLoginFail;
import ru.finex.gs.proto.helios.outcome.CharSelectionInfo;
import ru.finex.gs.proto.helios.outcome.CharacterSelected;
import ru.finex.gs.proto.helios.outcome.KeyPacket;
import ru.finex.gs.proto.helios.outcome.UserInfo;
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
        KeyPacket keyPacket = packetService.createOutcomePacket(0x2E);
        keyPacket.setKey(key);
        return keyPacket;
    }

    public L2GameServerPacket charSelectInfo(String login, int sessionId) {
        CharSelectionInfo packet = packetService.createOutcomePacket(0x0B);
        packet.setLogin(login);
        packet.setSessionId(sessionId);
        packet.setAvatars(avatarService.getAvatars(login));
        return packet;
    }

    public L2GameServerPacket authLoginFail(AuthFailReason reason) {
        AuthLoginFail packet = packetService.createOutcomePacket(0x0A);
        packet.setReason(reason);
        return packet;
    }

     public L2GameServerPacket serverClose() {
        return packetService.createOutcomePacket(0x20);
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
    public L2GameServerPacket castleManorList() {
        return packetService.createOutcomePacket(0xfe, 0x1b);
    }

    public L2GameServerPacket userInfo(GameObject gameObject) {
        UserInfo packet = packetService.createOutcomePacket(0x04);
        //FIXME finfan: тут должно быть что-то вроде INfoType? или opCodes отсылаемые и есть этот самый infoType?
        packet.setRuntimeId(gameObject.getRuntimeId());
        packet.setAbnormalComponent(gameObject.getComponent(AbnormalComponent.class));
        packet.setClanComponent(gameObject.getComponent(ClanComponent.class));
        packet.setClassComponent(gameObject.getComponent(ClassComponent.class));
        packet.setCollisionComponent(gameObject.getComponent(CollisionComponent.class));
        packet.setCoordinateComponent(gameObject.getComponent(CoordinateComponent.class));
        packet.setCubicComponent(gameObject.getComponent(CubicComponent.class));
        packet.setMountComponent(gameObject.getComponent(MountComponent.class));
        packet.setPlayerComponent(gameObject.getComponent(PlayerComponent.class));
        packet.setRecommendationComponent(gameObject.getComponent(RecommendationComponent.class));
        packet.setSpeedComponent(gameObject.getComponent(SpeedComponent.class));
        packet.setStateComponent(gameObject.getComponent(StateComponent.class));
        packet.setStatusComponent(gameObject.getComponent(StatusComponent.class));
        packet.setStoreComponent(gameObject.getComponent(StoreComponent.class));
        return packet;
    }
}
