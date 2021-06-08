package sf.finex.persistence;

import org.apache.commons.lang3.reflect.FieldUtils;
import sf.finex.GlobalContext;
import sf.finex.model.component.Component;
import sf.finex.model.entity.Entity;
import sf.finex.service.persistence.ComponentPersistenceService;
import sf.finex.service.persistence.PersistenceService;

import java.lang.reflect.Field;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ComponentPersistenceServiceImpl implements ComponentPersistenceService {

    @Override
    public void persist(Component component) {
        FieldUtils.getFieldsListWithAnnotation(component.getClass(), PersistenceField.class)
            .forEach(e -> persist(component, e, e.getAnnotation(PersistenceField.class).value()));
    }

    private void persist(Component component, Field field, Class<? extends PersistenceService> persistenceServiceType) {
        PersistenceService persistenceService = GlobalContext.injector.getInstance(persistenceServiceType);
        try {
            Entity entity = (Entity) FieldUtils.readField(field, component);
            if (entity != null) {
                persistenceService.persist(entity);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
