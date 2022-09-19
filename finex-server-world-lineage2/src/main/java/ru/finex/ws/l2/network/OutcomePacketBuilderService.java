package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.GameObject;
import ru.finex.network.netty.model.NetworkDto;
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
import ru.finex.ws.l2.model.AuthFailReason;
import ru.finex.ws.l2.model.dto.SelectedAvatarDto;
import ru.finex.ws.l2.network.model.dto.AuthLoginFailDto;
import ru.finex.ws.l2.network.model.dto.CharSelectInfoDto;
import ru.finex.ws.l2.network.model.dto.CharacterSelectedDto;
import ru.finex.ws.l2.network.model.dto.ManorListDto;
import ru.finex.ws.l2.network.model.dto.ServerCloseDto;
import ru.finex.ws.l2.network.model.dto.ServerKeyDto;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;
import ru.finex.ws.l2.network.session.GameClient;

import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class OutcomePacketBuilderService {

    private final AvatarService avatarService;
    private final ComponentService componentService;

    public NetworkDto keyPacket(byte[] key) {
        return ServerKeyDto.builder()
            .key(key)
            .serverId(0x01)
            .languageId(0x01) // EN/NA
            .build();
    }

    public NetworkDto charSelectInfo(String login, int sessionId) {
        return CharSelectInfoDto.builder()
            .login(login)
            .sessionId(sessionId)
            .avatars(avatarService.getAvatars(login))
            .build();
    }

    public NetworkDto authLoginFail(AuthFailReason reason) {
        return AuthLoginFailDto.builder()
            .messageId(reason.getMessageId())
            .isSuccess(reason.isSuccess())
            .build();
    }

    public NetworkDto characterSelected(GameObject gameObject) {
        ClientComponent clientComponent = componentService.getComponent(gameObject, ClientComponent.class);
        GameClient client = clientComponent.getClient();

        SelectedAvatarDto avatar = new SelectedAvatarDto();
        avatar.setPlayer(componentService.getComponent(gameObject, PlayerComponent.class).getEntity().clone());
        avatar.setClan(componentService.getComponent(gameObject, ClanComponent.class).getEntity().clone());
        avatar.setPosition(componentService.getComponent(gameObject, CoordinateComponent.class).getPosition().clone());
        avatar.setStatus(componentService.getComponent(gameObject, StatusComponent.class).getStatusEntity().clone());

        return CharacterSelectedDto.builder()
            .runtimeId(gameObject.getRuntimeId())
            .sessionId(0x00)
            .avatar(avatar)
            .build();
    }

    public NetworkDto serverClose() {
        return ServerCloseDto.INSTANCE;
    }
    
    public NetworkDto castleManorList() {
        return ManorListDto.builder()
            .castleIds(Collections.emptyList())
            .build();
    }
    
    public NetworkDto userInfo(GameObject gameObject) {
        return UserInfoDto.builder()
            .runtimeId(gameObject.getRuntimeId())
            .abnormalComponent(componentService.getComponent(gameObject, AbnormalComponent.class))
            .clanComponent(componentService.getComponent(gameObject, ClanComponent.class))
            .classComponent(componentService.getComponent(gameObject, ClassComponent.class))
            .collisionComponent(componentService.getComponent(gameObject, CollisionComponent.class))
            .coordinateComponent(componentService.getComponent(gameObject, CoordinateComponent.class))
            .cubicComponent(componentService.getComponent(gameObject, CubicComponent.class))
            .mountComponent(componentService.getComponent(gameObject, MountComponent.class))
            .playerComponent(componentService.getComponent(gameObject, PlayerComponent.class))
            .recommendationComponent(componentService.getComponent(gameObject, RecommendationComponent.class))
            .speedComponent(componentService.getComponent(gameObject, SpeedComponent.class))
            .stateComponent(componentService.getComponent(gameObject, StateComponent.class))
            .statusComponent(componentService.getComponent(gameObject, StatusComponent.class))
            .storeComponent(componentService.getComponent(gameObject, StoreComponent.class))
            .build();
    }
}
