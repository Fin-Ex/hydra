package ru.finex.gs.inject.module.gameplay;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import ru.finex.gs.model.GameObject;
import sf.l2j.gameserver.network.L2GameClient;

import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class PlayerModule extends AbstractModule {

    private final L2GameClient client;

    @Override
    protected void configure() {
        bind(L2GameClient.class).toInstance(client);
        bind(GameObject.class).toProvider((Provider<GameObject>) client::getGameObject);
    }
}
