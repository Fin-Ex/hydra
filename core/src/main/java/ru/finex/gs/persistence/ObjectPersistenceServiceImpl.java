package ru.finex.gs.persistence;

import org.apache.commons.lang3.reflect.FieldUtils;
import ru.finex.core.GlobalContext;
import ru.finex.gs.model.entity.Entity;
import ru.finex.gs.service.persistence.ObjectPersistenceService;
import ru.finex.gs.service.persistence.PersistenceService;

import java.lang.reflect.Field;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ObjectPersistenceServiceImpl implements ObjectPersistenceService {

    @Override
    public void persist(PersistenceObject object) {
        FieldUtils.getFieldsListWithAnnotation(object.getClass(), PersistenceField.class)
            .forEach(e -> persist(object, e, e.getAnnotation(PersistenceField.class).value()));
    }

    @Override
    public void restore(PersistenceObject object) {
        FieldUtils.getFieldsListWithAnnotation(object.getClass(), PersistenceField.class)
            .forEach(e -> restore(object, e, e.getAnnotation(PersistenceField.class).value()));
    }

    private void persist(PersistenceObject object, Field field, Class<? extends PersistenceService> persistenceServiceType) {
        PersistenceService persistenceService = GlobalContext.injector.getInstance(persistenceServiceType);
        try {
            Entity entity = (Entity) FieldUtils.readField(field, object);
            if (entity != null) {
                persistenceService.persist(entity);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private void restore(PersistenceObject object, Field field, Class<? extends PersistenceService> persistenceServiceType) {
        PersistenceService persistenceService = GlobalContext.injector.getInstance(persistenceServiceType);
        try {
            Entity entity = persistenceService.restore(object.getPersistenceId());
            FieldUtils.writeField(field, object, entity, true);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
