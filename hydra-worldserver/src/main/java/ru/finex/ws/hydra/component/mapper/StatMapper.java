package ru.finex.ws.hydra.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.hydra.component.StatComponent;
import ru.finex.ws.hydra.component.prototype.StatPrototype;

/**
 * @author m0nster.mind
 */
public class StatMapper implements ComponentPrototypeMapper<StatPrototype, StatComponent> {

    @Override
    public StatComponent map(StatPrototype prototype) {
        var component = new StatComponent();
        var entity = component.getEntity();
        entity.setPAtk(prototype.getPAtk());
        entity.setMAtk(prototype.getMAtk());
        entity.setPDef(prototype.getPDef());
        entity.setMDef(prototype.getMDef());
        entity.setCriticalRate(prototype.getCriticalRate());
        entity.setMagicCriticalRate(prototype.getMagicCriticalRate());
        entity.setAccuracy(prototype.getAccuracy());
        entity.setMagicAccuracy(prototype.getMagicAccuracy());
        entity.setEvasion(prototype.getEvasion());
        entity.setMagicEvasion(prototype.getMagicEvasion());
        return component;
    }

}
