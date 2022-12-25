package ru.finex.ws.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.ComponentService;
import ru.finex.core.hocon.ConfigResource;
import ru.finex.core.object.GameObject;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.ws.l2.component.AbnormalComponent;
import ru.finex.ws.l2.component.ClanComponent;
import ru.finex.ws.l2.component.ClassComponent;
import ru.finex.ws.l2.component.ClientComponent;
import ru.finex.ws.l2.component.ColliderComponent;
import ru.finex.ws.l2.component.CoordinateComponent;
import ru.finex.ws.l2.component.CubicComponent;
import ru.finex.ws.l2.component.MountComponent;
import ru.finex.ws.l2.component.ParameterComponent;
import ru.finex.ws.l2.component.PlayerComponent;
import ru.finex.ws.l2.component.RecommendationComponent;
import ru.finex.ws.l2.component.SpeedComponent;
import ru.finex.ws.l2.component.StatComponent;
import ru.finex.ws.l2.component.StateComponent;
import ru.finex.ws.l2.component.StatusComponent;
import ru.finex.ws.l2.component.StoreComponent;
import ru.finex.ws.l2.model.AuthFailReason;
import ru.finex.ws.l2.model.entity.AvatarPrototypeView;
import ru.finex.ws.l2.model.entity.PositionComponentEntity;
import ru.finex.ws.l2.model.entity.StateComponentEntity;
import ru.finex.ws.l2.model.enums.CharacterCreateReason;
import ru.finex.ws.l2.model.enums.CharacterNameReason;
import ru.finex.ws.l2.model.enums.Ex2ndPasswordReason;
import ru.finex.ws.l2.model.enums.RestartReason;
import ru.finex.ws.l2.network.model.UserInfoComponent;
import ru.finex.ws.l2.network.model.dto.AllFortressInfoDto;
import ru.finex.ws.l2.network.model.dto.AuthLoginFailDto;
import ru.finex.ws.l2.network.model.dto.ChangeMoveTypeDto;
import ru.finex.ws.l2.network.model.dto.CharacterCreateFailDto;
import ru.finex.ws.l2.network.model.dto.CharacterSelectInfoDto;
import ru.finex.ws.l2.network.model.dto.CharacterSelectedDto;
import ru.finex.ws.l2.network.model.dto.Ex2ndPasswordCheckDto;
import ru.finex.ws.l2.network.model.dto.IsCharacterNameCreatableDto;
import ru.finex.ws.l2.network.model.dto.LeaveWorldDto;
import ru.finex.ws.l2.network.model.dto.ManorListDto;
import ru.finex.ws.l2.network.model.dto.MoveToLocationDto;
import ru.finex.ws.l2.network.model.dto.NewCharacterSuccessDto;
import ru.finex.ws.l2.network.model.dto.RestartResponseDto;
import ru.finex.ws.l2.network.model.dto.ServerCloseDto;
import ru.finex.ws.l2.network.model.dto.StopMoveDto;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;
import ru.finex.ws.l2.network.model.dto.ValidateLocationDto;
import ru.finex.ws.l2.network.model.dto.VersionCheckDto;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.service.AvatarService;

import java.util.Collections;
import java.util.List;
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
    @ConfigResource(basePath = "ru.finex.ws.l2.service.SessionService")
    private int serverId;

    public NetworkDto versionCheck(byte[] key, boolean isValid) {
        return VersionCheckDto.builder()
            .key(key)
            .isValid(isValid)
            .serverId(0x01)
            .languageId(0x01) // EN/NA
            .build();
    }

    public NetworkDto charSelectInfo(String login, int sessionId) {
        return CharacterSelectInfoDto.builder()
            .login(login)
            .sessionId(sessionId)
            .avatars(avatarService.get(login))
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

        var player = componentService.getComponent(gameObject, PlayerComponent.class).getEntity();
        var clan = componentService.getComponent(gameObject, ClanComponent.class).getEntity();
        var position = componentService.getComponent(gameObject, CoordinateComponent.class).getEntity();
        var status = componentService.getComponent(gameObject, StatusComponent.class).getEntity();
        var clazz = componentService.getComponent(gameObject, ClassComponent.class).getEntity();


        return CharacterSelectedDto.builder()
            .runtimeId(gameObject.getRuntimeId())
            .sessionId(client.getData().getSessionId())
            .name(player.getName())
            .title(player.getTitle())
            .race(player.getRace())
            .gender(player.getGender())
            .appearanceClass(player.getAppearanceClass())
            .classId(clazz.getClassId())
            .clanId(clan.getClanId())
            .x(position.getX())
            .y(position.getY())
            .z(position.getZ())
            .hp(status.getHp())
            .mp(status.getMp())
            .sp(clazz.getSp())
            .exp(clazz.getExp())
            .level(clazz.getLevel())
            .build();
    }

    public NetworkDto newCharacterSuccess(List<AvatarPrototypeView> prototypes) {
        return new NewCharacterSuccessDto(prototypes);
    }

    public NetworkDto characterNameCreatable(CharacterNameReason reason) {
        return new IsCharacterNameCreatableDto(reason.getId());
    }

    public NetworkDto charCreateFail(CharacterCreateReason reason) {
        return new CharacterCreateFailDto(reason.getId());
    }

    public NetworkDto passwordCheck(Ex2ndPasswordReason reason) {
        return new Ex2ndPasswordCheckDto(reason.getId());
    }

    public NetworkDto serverClose() {
        return ServerCloseDto.INSTANCE;
    }
    
    public NetworkDto castleManorList() {
        return new ManorListDto(Collections.emptyList());
    }

    public NetworkDto allFortressInfo() {
        return new AllFortressInfoDto(Collections.emptyList());
    }
    
    public NetworkDto userInfo(GameObject gameObject) {
        return UserInfoDto.builder()
            .runtimeId(gameObject.getRuntimeId())
            .playerComponent(componentService.getComponent(gameObject, PlayerComponent.class))
            .collisionComponent(componentService.getComponent(gameObject, ColliderComponent.class))
            .speedComponent(componentService.getComponent(gameObject, SpeedComponent.class))
            .cubicComponent(componentService.getComponent(gameObject, CubicComponent.class))
            .stateComponent(componentService.getComponent(gameObject, StateComponent.class))
            .abnormalComponent(componentService.getComponent(gameObject, AbnormalComponent.class))
            .clanComponent(componentService.getComponent(gameObject, ClanComponent.class))
            .recommendationComponent(componentService.getComponent(gameObject, RecommendationComponent.class))
            .mountComponent(componentService.getComponent(gameObject, MountComponent.class))
            .classComponent(componentService.getComponent(gameObject, ClassComponent.class))
            .storeComponent(componentService.getComponent(gameObject, StoreComponent.class))
            .coordinateComponent(componentService.getComponent(gameObject, CoordinateComponent.class))
            .statusComponent(componentService.getComponent(gameObject, StatusComponent.class))
            .parameterComponent(componentService.getComponent(gameObject, ParameterComponent.class))
            .statComponent(componentService.getComponent(gameObject, StatComponent.class))
            .components(UserInfoComponent.all())
            .build();
    }

    public NetworkDto restart(RestartReason reason) {
        return new RestartResponseDto(reason.getId());
    }

    public NetworkDto leaveWorld() {
        return LeaveWorldDto.INSTANCE;
    }

    public NetworkDto validateLocation(GameObject gameObject) {
        CoordinateComponent component = componentService.getComponent(gameObject, CoordinateComponent.class);
        return ValidateLocationDto.builder()
            .heading((int) component.getEntity().getH())
            .x(component.getEntity().getX().intValue())
            .y(component.getEntity().getY().intValue())
            .z(component.getEntity().getZ().intValue())
            .vehicleId(0x00) // FIXME finfan: must be a vehicle id
            .build();
    }

    public NetworkDto moveToLocation(GameObject gameObject) {
        CoordinateComponent component = componentService.getComponent(gameObject, CoordinateComponent.class);
        return MoveToLocationDto.builder()
            .runtimeId(gameObject.getRuntimeId())
            .positionX(component.getEntity().getX().intValue())
            .positionY(component.getEntity().getY().intValue())
            .positionZ(component.getEntity().getZ().intValue())
            .destinationX((int) component.getDestination().getX())
            .destinationY((int) component.getDestination().getY())
            .destinationZ((int) component.getDestination().getZ())
            .build();
    }

    public NetworkDto stopMove(GameObject gameObject) {
        CoordinateComponent component = componentService.getComponent(gameObject, CoordinateComponent.class);
        PositionComponentEntity position = component.getEntity();
        return StopMoveDto.builder()
            .runtimeId(gameObject.getRuntimeId())
            .x(position.getX().intValue())
            .y(position.getY().intValue())
            .z(position.getZ().intValue())
            .heading((int) position.getH())
            .build();
    }

    public NetworkDto changeMoveType(GameObject gameObject) {
        StateComponent component = componentService.getComponent(gameObject, StateComponent.class);
        StateComponentEntity entity = component.getEntity();
        return ChangeMoveTypeDto.builder()
            .runtimeId(gameObject.getRuntimeId())
            .isRunning(entity.getIsRunning())
            .build();
    }

}
