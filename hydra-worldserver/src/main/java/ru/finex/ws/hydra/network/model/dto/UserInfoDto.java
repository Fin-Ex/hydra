package ru.finex.ws.hydra.network.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.ws.hydra.component.CoordinateComponent;
import ru.finex.ws.hydra.component.ParameterComponent;
import ru.finex.ws.hydra.component.StatComponent;
import ru.finex.ws.hydra.component.StatusComponent;
import ru.finex.ws.hydra.component.AbnormalComponent;
import ru.finex.ws.hydra.component.ClanComponent;
import ru.finex.ws.hydra.component.ClassComponent;
import ru.finex.ws.hydra.component.ColliderComponent;
import ru.finex.ws.hydra.component.CubicComponent;
import ru.finex.ws.hydra.component.MountComponent;
import ru.finex.ws.hydra.component.PlayerComponent;
import ru.finex.ws.hydra.component.RecommendationComponent;
import ru.finex.ws.hydra.component.SpeedComponent;
import ru.finex.ws.hydra.component.StateComponent;
import ru.finex.ws.hydra.component.StoreComponent;
import ru.finex.ws.hydra.network.model.UserInfoComponent;

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
