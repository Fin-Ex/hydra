package ru.finex.ws.l2.component.mapper;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.Component;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.core.prototype.ComponentPrototypeMapper.Register;
import ru.finex.core.utils.ClassUtils;
import ru.finex.ws.l2.component.base.ParameterComponent;
import ru.finex.ws.l2.component.base.StatComponent;
import ru.finex.ws.l2.component.player.AbnormalComponent;
import ru.finex.ws.l2.component.player.ClanComponent;
import ru.finex.ws.l2.component.player.ClassComponent;
import ru.finex.ws.l2.component.player.ClientComponent;
import ru.finex.ws.l2.component.player.CollisionComponent;
import ru.finex.ws.l2.component.player.CubicComponent;
import ru.finex.ws.l2.component.player.MountComponent;
import ru.finex.ws.l2.component.player.RecommendationComponent;
import ru.finex.ws.l2.component.player.SpeedComponent;
import ru.finex.ws.l2.component.player.StateComponent;
import ru.finex.ws.l2.component.player.StoreComponent;
import ru.finex.ws.l2.component.player.VisualEquipComponent;
import ru.finex.ws.l2.component.prototype.AbnormalPrototype;
import ru.finex.ws.l2.component.prototype.ClanPrototype;
import ru.finex.ws.l2.component.prototype.ClassPrototype;
import ru.finex.ws.l2.component.prototype.ClientPrototype;
import ru.finex.ws.l2.component.prototype.CollisionPrototype;
import ru.finex.ws.l2.component.prototype.CubicPrototype;
import ru.finex.ws.l2.component.prototype.MountPrototype;
import ru.finex.ws.l2.component.prototype.ParameterPrototype;
import ru.finex.ws.l2.component.prototype.RecommendationPrototype;
import ru.finex.ws.l2.component.prototype.SpeedPrototype;
import ru.finex.ws.l2.component.prototype.StatPrototype;
import ru.finex.ws.l2.component.prototype.StatePrototype;
import ru.finex.ws.l2.component.prototype.StorePrototype;
import ru.finex.ws.l2.component.prototype.VisualEquipPrototype;

/**
 * @author m0nster.mind
 */
@Register(prototype = AbnormalPrototype.class, component = AbnormalComponent.class)
@Register(prototype = ClanPrototype.class, component = ClanComponent.class)
@Register(prototype = ClassPrototype.class, component = ClassComponent.class)
@Register(prototype = ClientPrototype.class, component = ClientComponent.class)
@Register(prototype = CollisionPrototype.class, component = CollisionComponent.class)
@Register(prototype = CubicPrototype.class, component = CubicComponent.class)
@Register(prototype = MountPrototype.class, component = MountComponent.class)
@Register(prototype = RecommendationPrototype.class, component = RecommendationComponent.class)
@Register(prototype = SpeedPrototype.class, component = SpeedComponent.class)
@Register(prototype = StatePrototype.class, component = StateComponent.class)
@Register(prototype = StorePrototype.class, component = StoreComponent.class)
@Register(prototype = VisualEquipPrototype.class, component = VisualEquipComponent.class)
@Register(prototype = StatPrototype.class, component = StatComponent.class)
@Register(prototype = ParameterPrototype.class, component = ParameterComponent.class)
@RequiredArgsConstructor
public class EmptyMapper<P extends ComponentPrototype, C extends Component> implements ComponentPrototypeMapper<P, C> {

    private final Class<?> componentType;

    @Override
    public C map(P prototype) {
        return (C) ClassUtils.createInstance(componentType);
    }

}
