package ru.finex.ws.hydra.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.hydra.component.ClassComponent;
import ru.finex.ws.hydra.component.prototype.ClassPrototype;
import ru.finex.ws.hydra.model.enums.ClassId;

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
