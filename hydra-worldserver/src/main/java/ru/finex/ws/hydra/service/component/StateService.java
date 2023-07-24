package ru.finex.ws.hydra.service.component;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.impl.AbstractComponentLogicService;
import ru.finex.core.object.GameObject;
import ru.finex.ws.hydra.component.StateComponent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class StateService extends AbstractComponentLogicService<StateComponent> {

    public void setRunning(GameObject gameObject, boolean isRunning) {
        StateComponent component = getComponent(gameObject);
        component.getEntity().setIsRunning(isRunning);
    }

    public void toggleRunning(GameObject gameObject) {
        StateComponent component = getComponent(gameObject);
        component.toggleRunning();
    }

    public void toggleSitting(GameObject gameObject) {
        StateComponent component = getComponent(gameObject);
        component.toggleSitting();
    }

}
