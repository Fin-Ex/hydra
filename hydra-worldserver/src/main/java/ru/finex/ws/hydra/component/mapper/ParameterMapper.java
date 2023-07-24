package ru.finex.ws.hydra.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.hydra.component.ParameterComponent;
import ru.finex.ws.hydra.component.prototype.ParameterPrototype;

/**
 * @author m0nster.mind
 */
public class ParameterMapper implements ComponentPrototypeMapper<ParameterPrototype, ParameterComponent> {

    @Override
    public ParameterComponent map(ParameterPrototype prototype) {
        var component = new ParameterComponent();
        var entity = component.getEntity();
        entity.setINT(prototype.getINT());
        entity.setSTR(prototype.getSTR());
        entity.setMEN(prototype.getMEN());
        entity.setDEX(prototype.getDEX());
        entity.setCON(prototype.getCON());
        entity.setWIT(prototype.getWIT());
        entity.setCHA(prototype.getCHA());
        entity.setLUC(prototype.getLUC());
        return component;
    }

}
