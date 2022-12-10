package ru.finex.ws.l2.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.l2.component.base.CoordinateComponent;
import ru.finex.ws.l2.component.prototype.CoordinatePrototype;

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
