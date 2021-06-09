package ru.finex.gs.model.component;

import ru.finex.gs.model.GameObject;
import ru.finex.gs.persistence.PersistenceObject;

import java.util.Comparator;

/**
 * @author m0nster.mind
 */
public interface Component extends PersistenceObject, Comparable<Component> {

    int ORDER_PRIORITY_FIRST = 1;
    int ORDER_PRIORITY_NORMAL = 5;
    int ORDER_PRIORITY_LAST = 9999;

    Comparator<Component> COMPARATOR = Component::compareTo;

    GameObject getGameObject();
    void setGameObject(GameObject gameObject);

    /** Компонент был подключен к игровому объекту. Производится до момента восстановления данных из БД. */
    void onAttached();
    /** Компонент был восстановлен из БД. */
    void onRestored();
    /** Компонент был отключен от игрового объекта. */
    void onDeattached();

    void onPreUpdate();
    void onUpdate();
    void onPostUpdate();

    default boolean isType(Class<?> type) {
        return getClass().isInstance(type);
    }

    default boolean isChildOf(Class<? extends Component> type) {
        return getClass().isAssignableFrom(type);
    }

    default int getExecutePriority() {
        return ORDER_PRIORITY_NORMAL;
    }

    @Override
    default int compareTo(Component o) {
        int priority = getExecutePriority();
        int otherPriority = o.getExecutePriority();
        return Integer.compare(priority, otherPriority);
    }

    @Override
    default int getPersistenceId() {
        return getGameObject().getPersistenceId();
    }
}
