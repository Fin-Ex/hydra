package ru.finex.ws.hydra.component;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import ru.finex.core.persistence.PersistenceField;
import ru.finex.ws.hydra.model.entity.StateComponentEntity;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StateComponent extends AbstractComponent {

    @Setter(AccessLevel.NONE)
    @PersistenceField
    private StateComponentEntity entity = new StateComponentEntity();

    private boolean isBattleStance;
    private boolean isDeath;
    private boolean isInvisible;
    private boolean isSearchParty;
    private boolean isHeroAura;
    private boolean isNoble;

    public boolean toggleRunning() {
        entity.setIsRunning(!entity.getIsRunning());
        return entity.getIsRunning();
    }

    public boolean toggleSitting() {
        entity.setIsSitting(!entity.getIsSitting());
        return entity.getIsSitting();
    }

}
