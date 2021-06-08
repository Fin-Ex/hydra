package sf.finex.model;

import com.google.inject.Module;
import lombok.RequiredArgsConstructor;
import sf.finex.GameServerContext;
import sf.finex.inject.GameplayModule;
import sf.finex.inject.module.gameplay.GameObjectModule;
import sf.finex.inject.module.gameplay.PlayerModule;
import sf.finex.model.component.Component;
import sf.finex.model.component.base.CoordinateComponent;
import sf.finex.model.component.player.AbnormalComponent;
import sf.finex.model.component.player.ClanComponent;
import sf.finex.model.component.player.ClassComponent;
import sf.finex.model.component.player.ClientComponent;
import sf.finex.model.component.player.CollisionComponent;
import sf.finex.model.component.player.CubicComponent;
import sf.finex.model.component.player.MountComponent;
import sf.finex.model.component.player.NetworkComponent;
import sf.finex.model.component.player.PlayerComponent;
import sf.finex.model.component.player.RecommendationComponent;
import sf.finex.model.component.player.SpeedComponent;
import sf.finex.model.component.player.StateComponent;
import sf.finex.model.component.player.StoreComponent;
import sf.finex.model.component.player.VisualEquipComponent;
import sf.finex.service.RuntimeIdService;
import sf.finex.utils.Classes;
import sf.finex.utils.InjectorUtils;
import sf.l2j.gameserver.network.L2GameClient;

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
        CoordinateComponent.class
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
    private final GameServerContext ctx;

    public GameObject createPlayer(L2GameClient client, int persistenceId) {
        int runtimeId = runtimeIdService.generateId();

        GameObject gameObject = new GameObject(runtimeId, persistenceId);

        List<Module> modules = InjectorUtils.collectModules(GameplayModule.class);
        modules.add(new PlayerModule(client));
        modules.add(new GameObjectModule(gameObject));
        gameObject.setInjector(ctx.injector.createChildInjector(modules));

        addBaseComponents(gameObject);
        addPlayerComponents(gameObject);

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
