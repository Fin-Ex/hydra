package ru.finex.gs.service;

import com.google.inject.ImplementedBy;
import ru.finex.gs.model.GameObject;
import ru.finex.gs.world.WorldServiceImpl;

import java.util.Collection;

/**
 * @author m0nster.mind
 */
@ImplementedBy(WorldServiceImpl.class)
public interface WorldService {

    void addGameObject(GameObject gameObject);
    void removeGameObject(GameObject gameObject);
    GameObject getGameObject(int runtimeId);
    Collection<GameObject> getGameObjects();

}
