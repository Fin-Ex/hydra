package ru.finex.ws.l2.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.l2.component.ClassComponent;
import ru.finex.ws.l2.component.prototype.ClassPrototype;
import ru.finex.ws.l2.model.enums.ClassId;

/**
 * @author m0nster.mind
 */
public class ClassMapper implements ComponentPrototypeMapper<ClassPrototype, ClassComponent> {

    @Override
    public ClassComponent map(ClassPrototype prototype) {
        var component = new ClassComponent();
        var entity = component.getEntity();
        entity.setClassId(ClassId.ofId(prototype.getClassId()));
        entity.setLevel(1);
        entity.setExp(0L);
        entity.setSp(0L);
        return component;
    }

}
