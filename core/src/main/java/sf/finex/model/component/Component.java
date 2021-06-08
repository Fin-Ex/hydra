package sf.finex.model.component;

import sf.finex.model.GameObject;

import java.util.Comparator;

/**
 * @author m0nster.mind
 */
public interface Component extends Comparable<Component> {

    int ORDER_PRIORITY_FIRST = 1;
    int ORDER_PRIORITY_NORMAL = 5;
    int ORDER_PRIORITY_LAST = 9999;

    Comparator<Component> COMPARATOR = Component::compareTo;

    GameObject getGameObject();
    void setGameObject(GameObject gameObject);

    void onAttached();
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
}
