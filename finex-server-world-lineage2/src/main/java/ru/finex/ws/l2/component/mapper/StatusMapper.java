package ru.finex.ws.l2.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.l2.component.StatusComponent;
import ru.finex.ws.l2.component.prototype.StatusPrototype;

/**
 * @author m0nster.mind
 */
public class StatusMapper implements ComponentPrototypeMapper<StatusPrototype, StatusComponent> {

    @Override
    public StatusComponent map(StatusPrototype prototype) {
        var component = new StatusComponent();
        var entity = component.getEntity();
        entity.setCp(prototype.getCp());
        entity.setHp(prototype.getHp());
        entity.setMp(prototype.getMp());
        entity.setMaxCp(prototype.getMaxCp());
        entity.setMaxHp(prototype.getMaxHp());
        entity.setMaxMp(prototype.getMaxMp());
        return component;
    }

}
