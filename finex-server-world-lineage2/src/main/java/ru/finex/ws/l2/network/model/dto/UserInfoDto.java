package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.ws.l2.component.base.CoordinateComponent;
import ru.finex.ws.l2.component.base.ParameterComponent;
import ru.finex.ws.l2.component.base.StatComponent;
import ru.finex.ws.l2.component.base.StatusComponent;
import ru.finex.ws.l2.component.player.AbnormalComponent;
import ru.finex.ws.l2.component.player.ClanComponent;
import ru.finex.ws.l2.component.player.ClassComponent;
import ru.finex.ws.l2.component.player.CollisionComponent;
import ru.finex.ws.l2.component.player.CubicComponent;
import ru.finex.ws.l2.component.player.MountComponent;
import ru.finex.ws.l2.component.player.PlayerComponent;
import ru.finex.ws.l2.component.player.RecommendationComponent;
import ru.finex.ws.l2.component.player.SpeedComponent;
import ru.finex.ws.l2.component.player.StateComponent;
import ru.finex.ws.l2.component.player.StoreComponent;
import ru.finex.ws.l2.network.model.UserInfoComponent;

import java.util.List;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto implements NetworkDto {

    // FIXME m0nster.mind: инкапулировать данные сюда, без entity

    private int runtimeId;
    private PlayerComponent playerComponent;
    private CollisionComponent collisionComponent;
    private SpeedComponent speedComponent;
    private CubicComponent cubicComponent;
    private StateComponent stateComponent;
    private AbnormalComponent abnormalComponent;
    private ClanComponent clanComponent;
    private RecommendationComponent recommendationComponent;
    private MountComponent mountComponent;
    private ClassComponent classComponent;
    private StoreComponent storeComponent;
    private CoordinateComponent coordinateComponent;
    private StatusComponent statusComponent;
    private ParameterComponent parameterComponent;
    private StatComponent statComponent;

    private List<UserInfoComponent> components;

    public boolean containsComponent(UserInfoComponent component) {
        return components.contains(component);
    }

    public byte[] mask() {
        byte[] flags = new byte[UserInfoComponent.octets()];
        for (int i = 0; i < components.size(); i++) {
            var component = components.get(i);
            flags[component.getPosition()] |= component.getFlag();
        }

        return flags;
    }

}
