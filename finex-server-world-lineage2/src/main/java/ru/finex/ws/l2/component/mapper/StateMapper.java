package ru.finex.ws.l2.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.l2.component.StateComponent;
import ru.finex.ws.l2.component.prototype.StatePrototype;

/**
 * @author m0nster.mind
 */
public class StateMapper implements ComponentPrototypeMapper<StatePrototype, StateComponent> {

    @Override
    public StateComponent map(StatePrototype prototype) {
        var component = new StateComponent();
        var entity = component.getEntity();
        entity.setIsRunning(prototype.isRunning());
        entity.setIsSitting(prototype.isSitting());
        return component;
    }

}
