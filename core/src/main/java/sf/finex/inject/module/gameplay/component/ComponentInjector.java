package sf.finex.inject.module.gameplay.component;

import com.google.inject.MembersInjector;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import sf.finex.model.GameObject;
import sf.finex.model.component.Component;

import java.lang.reflect.Field;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class ComponentInjector<T> implements MembersInjector<T> {

    private final Field field;
    private final GameObject gameObject;

    @SneakyThrows
    @Override
    public void injectMembers(T instance) {
        Component component = gameObject.getComponent((Class<? extends Component>) field.getType());
        FieldUtils.writeField(field, instance, component, true);
    }

}
