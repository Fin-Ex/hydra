package ru.finex.ws.l2.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.l2.component.player.ClassComponent;
import ru.finex.ws.l2.component.prototype.ClassPrototype;
import ru.finex.ws.l2.model.ClassId;

/**
 * @author m0nster.mind
 */
public class ClassMapper implements ComponentPrototypeMapper<ClassPrototype, ClassComponent> {

    @Override
    public ClassComponent map(ClassPrototype prototype) {
        var component = new ClassComponent();
        component.setClassId(ClassId.ofId(prototype.getClassId()));
        return component;
    }

}
