package ru.finex.ws.l2.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.l2.component.ColliderComponent;
import ru.finex.ws.l2.component.prototype.ColliderPrototype;

/**
 * @author m0nster.mind
 */
public class ColliderMapper implements ComponentPrototypeMapper<ColliderPrototype, ColliderComponent> {

    @Override
    public ColliderComponent map(ColliderPrototype prototype) {
        var component = new ColliderComponent();
        component.setHeight(prototype.getHeight());
        component.setWidth(prototype.getWidth());
        return component;
    }

}
