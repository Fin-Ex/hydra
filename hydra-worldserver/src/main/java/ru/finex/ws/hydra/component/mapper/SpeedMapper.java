package ru.finex.ws.hydra.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.hydra.component.SpeedComponent;
import ru.finex.ws.hydra.component.prototype.SpeedPrototype;

/**
 * @author m0nster.mind
 */
public class SpeedMapper implements ComponentPrototypeMapper<SpeedPrototype, SpeedComponent> {

    @Override
    public SpeedComponent map(SpeedPrototype prototype) {
        var component = new SpeedComponent();
        component.setRunSpeed(prototype.getRunSpeed());
        component.setWalkSpeed(prototype.getWalkSpeed());
        component.setSwimSpeed(prototype.getSwimSpeed());
        component.setAttackSpeed(prototype.getAttackSpeed());
        component.setCastSpeed(prototype.getCastSpeed());
        return component;
    }

}
