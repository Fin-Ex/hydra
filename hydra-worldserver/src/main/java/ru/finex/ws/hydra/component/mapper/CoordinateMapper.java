package ru.finex.ws.hydra.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.hydra.component.CoordinateComponent;
import ru.finex.ws.hydra.component.prototype.CoordinatePrototype;

/**
 * @author m0nster.mind
 */
public class CoordinateMapper implements ComponentPrototypeMapper<CoordinatePrototype, CoordinateComponent> {

    @Override
    public CoordinateComponent map(CoordinatePrototype prototype) {
        var component = new CoordinateComponent();
        component.setPosition(prototype.getX(), prototype.getY(), prototype.getZ());
        return component;
    }

}
