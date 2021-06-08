package sf.finex.inject.module.gameplay;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import sf.finex.inject.module.gameplay.component.GameObjectComponentModule;
import sf.finex.model.GameObject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class GameObjectModule extends AbstractModule {

    private final GameObject gameObject;

    @Override
    protected void configure() {
        install(new GameObjectComponentModule(gameObject));
    }
}
