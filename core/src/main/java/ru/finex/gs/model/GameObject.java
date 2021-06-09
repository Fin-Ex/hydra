package ru.finex.gs.model;

import com.google.inject.Injector;
import lombok.Getter;
import lombok.Setter;
import ru.finex.core.events.EventBus;
import ru.finex.gs.model.component.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author m0nster.mind
 */
public class GameObject {

    private final List<Component> components = new ArrayList<>();
    private final ReadWriteLock componentsRwLock = new ReentrantReadWriteLock();

    @Getter private final EventBus eventBus = new EventBus();

    /** Динамический ID игрового объекта, который существует только в рамках сессии этого игрового объекта. */
    @Getter private final int runtimeId;
    /** Персистентный ID игрового объекта, который хранится в БД. */
    @Getter private final int persistenceId;
    /** Инжектор, который хранит в себе контекст для инжектирования связанный именно с этим игровым объектом. */
    @Getter @Setter private Injector injector;

    public GameObject(int runtimeId, int persistenceId) {
        this.runtimeId = runtimeId;
        this.persistenceId = persistenceId;
    }

    public void addComponent(Component component) {
        if (component.getGameObject() != null) {
            throw new RuntimeException(String.format("Trying attach component to %s, component already attached to %s game object.",
                this.toString(),
                component.getGameObject().toString())
            );
        }

        component.setGameObject(this);
        injector.injectMembers(component);

        Lock lock = componentsRwLock.writeLock();
        lock.lock();
        try {
            components.add(component);
            components.sort(Component.COMPARATOR);
        } finally {
            lock.unlock();
        }

        component.onAttached();
    }

    public void addComponent(Class<? extends Component> componentType) {
        Component component = injector.getInstance(componentType);
        component.setGameObject(this);

        Lock lock = componentsRwLock.writeLock();
        lock.lock();
        try {
            components.add(component);
            components.sort(Component.COMPARATOR);
        } finally {
            lock.unlock();
        }

        component.onAttached();
    }

    public boolean removeComponent(Component component) {
        Lock lock = componentsRwLock.writeLock();
        lock.lock();
        try {
            if (!components.remove(component)) {
                return false;
            }
        } finally {
            lock.unlock();
        }

        component.onDeattached();
        component.setGameObject(null);
        return true;
    }

    public boolean removeComponent(Class<? extends Component> componentType) {
        Component component = getComponent(componentType);
        if (component == null) {
            return false;
        }

        Lock lock = componentsRwLock.writeLock();
        lock.lock();
        try {
            components.remove(component);
        } finally {
            lock.unlock();
        }

        component.onDeattached();
        component.setGameObject(null);
        return true;
    }

    public <T extends Component> T getComponent(Class<T> componentType) {
        Lock lock = componentsRwLock.readLock();
        lock.lock();
        try {
            return (T) components.stream()
                .filter(e -> e.isChildOf(componentType))
                .findAny()
                .orElse(null);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Возвращает копию списка компонентов.
     *
     * @return компоненты игрового объекта
     */
    public List<Component> getComponents() {
        Lock lock = componentsRwLock.readLock();
        lock.lock();
        try {
            return new ArrayList<>(components);
        } finally {
            lock.unlock();
        }
    }

}
