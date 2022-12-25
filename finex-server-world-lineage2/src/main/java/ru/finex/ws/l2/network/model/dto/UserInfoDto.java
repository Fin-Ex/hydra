package ru.finex.ws.l2.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.ws.l2.component.CoordinateComponent;
import ru.finex.ws.l2.component.ParameterComponent;
import ru.finex.ws.l2.component.StatComponent;
import ru.finex.ws.l2.component.StatusComponent;
import ru.finex.ws.l2.component.AbnormalComponent;
import ru.finex.ws.l2.component.ClanComponent;
import ru.finex.ws.l2.component.ClassComponent;
import ru.finex.ws.l2.component.ColliderComponent;
import ru.finex.ws.l2.component.CubicComponent;
import ru.finex.ws.l2.component.MountComponent;
import ru.finex.ws.l2.component.PlayerComponent;
import ru.finex.ws.l2.component.RecommendationComponent;
import ru.finex.ws.l2.component.SpeedComponent;
import ru.finex.ws.l2.component.StateComponent;
import ru.finex.ws.l2.component.StoreComponent;
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
    private ColliderComponent collisionComponent;
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

    public int flags() {
        int flags = 0;
        for (int i = 0; i < components.size(); i++) {
            var component = components.get(i);
            flags |= component.getFlag();
        }

        return flags;
    }

}
