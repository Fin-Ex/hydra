package sf.finex.inject.module.gameplay.component;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import lombok.RequiredArgsConstructor;
import sf.finex.model.GameObject;

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
