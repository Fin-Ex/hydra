package sf.finex.inject.module.gameplay.component;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import sf.finex.inject.InjectComponent;
import sf.finex.model.GameObject;
import sf.finex.model.component.Component;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class ComponentTypeListener implements TypeListener {

    private final GameObject gameObject;

    @Override
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        Class<? super I> type = typeLiteral.getRawType();
        FieldUtils.getFieldsListWithAnnotation(type, InjectComponent.class)
            .stream()
            .filter(field -> Component.class.isAssignableFrom(field.getType()))
            .forEach(field -> typeEncounter.register(new ComponentInjector<>(field, gameObject)));
    }

}
