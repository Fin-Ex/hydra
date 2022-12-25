package ru.finex.ws.l2.component.mapper;

import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.ws.l2.component.PlayerComponent;
import ru.finex.ws.l2.component.prototype.PlayerPrototype;
import ru.finex.ws.l2.model.PvpMode;

/**
 * @author m0nster.mind
 */
public class PlayerMapper implements ComponentPrototypeMapper<PlayerPrototype, PlayerComponent> {

    @Override
    public PlayerComponent map(PlayerPrototype prototype) {
        var component = new PlayerComponent();
        var entity = component.getEntity();
        entity.setRace(prototype.getRace());
        entity.setGender(prototype.getGender());
        entity.setAppearanceClass(prototype.getAppearanceClass());
        entity.setHairType(prototype.getHairType());
        entity.setHairColor(prototype.getHairColor());
        entity.setPvpMode(PvpMode.NONE);
        entity.setNameColor(prototype.getNameColor());
        entity.setTitleColor(prototype.getTitleColor());
        return component;
    }

}
