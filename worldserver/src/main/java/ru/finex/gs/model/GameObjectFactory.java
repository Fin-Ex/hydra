package ru.finex.gs.model;

import com.google.inject.Module;
import lombok.RequiredArgsConstructor;
import ru.finex.core.ServerContext;
import ru.finex.core.utils.Classes;
import ru.finex.core.utils.InjectorUtils;
import ru.finex.gs.inject.GameplayModule;
import ru.finex.gs.inject.module.gameplay.GameObjectModule;
import ru.finex.gs.inject.module.gameplay.PlayerModule;
import ru.finex.gs.model.component.Component;
import ru.finex.gs.model.component.base.CoordinateComponent;
import ru.finex.gs.model.component.base.StatusComponent;
import ru.finex.gs.model.component.player.AbnormalComponent;
import ru.finex.gs.model.component.player.ClanComponent;
import ru.finex.gs.model.component.player.ClassComponent;
import ru.finex.gs.model.component.player.ClientComponent;
import ru.finex.gs.model.component.player.CollisionComponent;
import ru.finex.gs.model.component.player.CubicComponent;
import ru.finex.gs.model.component.player.MountComponent;
import ru.finex.gs.model.component.player.NetworkComponent;
import ru.finex.gs.model.component.player.PlayerComponent;
import ru.finex.gs.model.component.player.RecommendationComponent;
import ru.finex.gs.model.component.player.SpeedComponent;
import ru.finex.gs.model.component.player.StateComponent;
import ru.finex.gs.model.component.player.StoreComponent;
import ru.finex.gs.model.component.player.VisualEquipComponent;
import ru.finex.gs.service.RuntimeIdService;
import ru.finex.gs.service.WorldService;
import ru.finex.gs.service.persistence.GameObjectPersistenceService;

import java.util.List;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectFactory {

    private static final Class[] BASE_COMPONENTS = {
        CoordinateComponent.class,
        StatusComponent.class
    };

    private static final Class[] PLAYER_COMPONENTS = {
        AbnormalComponent.class,
        ClanComponent.class,
        ClassComponent.class,
        ClientComponent.class,
        CollisionComponent.class,
        CubicComponent.class,
        MountComponent.class,
        NetworkComponent.class,
        PlayerComponent.class,
        RecommendationComponent.class,
        SpeedComponent.class,
        StateComponent.class,
        StoreComponent.class,
        VisualEquipComponent.class
    };

    private final RuntimeIdService runtimeIdService;
    private final GameObjectPersistenceService persistenceService;
    private final WorldService worldService;
    private final ServerContext ctx;

    public GameObject createPlayer(Client client, int persistenceId) {
        int runtimeId = runtimeIdService.generateId();

        GameObject gameObject = new GameObject(runtimeId, persistenceId);

        List<Module> modules = InjectorUtils.collectModules(GameplayModule.class);
        modules.add(new PlayerModule(client));
        modules.add(new GameObjectModule(gameObject));
        gameObject.setInjector(ctx.getInjector().createChildInjector(modules));

        addBaseComponents(gameObject);
        addPlayerComponents(gameObject);

        persistenceService.restore(gameObject);
        worldService.addGameObject(gameObject);

        return gameObject;
    }

    private void addBaseComponents(GameObject gameObject) {
        addComponents(gameObject, BASE_COMPONENTS);
    }

    private void addPlayerComponents(GameObject gameObject) {
        addComponents(gameObject, PLAYER_COMPONENTS);
    }

    private static void addComponents(GameObject gameObject, Class[] componentTypes) {
        Stream.of(componentTypes)
            .map(Classes::createInstance)
            .map(e -> (Component) e)
            .sorted()
            .forEachOrdered(gameObject::addComponent);
    }

}
