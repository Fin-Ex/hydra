package ru.finex.gs.world;

import ru.finex.gs.model.GameObject;
import ru.finex.gs.service.WorldService;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class WorldServiceImpl implements WorldService {

    private final Map<Integer, GameObject> objects = new HashMap<>();

    @Override
    public void addGameObject(GameObject gameObject) {
        objects.put(gameObject.getRuntimeId(), gameObject);
    }

    @Override
    public void removeGameObject(GameObject gameObject) {
        objects.remove(gameObject.getRuntimeId());
    }

    @Override
    public GameObject getGameObject(int runtimeId) {
        return objects.get(runtimeId);
    }
}
