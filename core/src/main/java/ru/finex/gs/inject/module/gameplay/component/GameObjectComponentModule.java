package ru.finex.gs.inject.module.gameplay.component;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import lombok.RequiredArgsConstructor;
import ru.finex.gs.model.GameObject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class GameObjectComponentModule extends AbstractModule {

    private final GameObject gameObject;

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new ComponentTypeListener(gameObject));
    }

}
