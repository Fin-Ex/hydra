package ru.finex.core.db;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.net.URL;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class ServiceRegistryProvider implements Provider<ServiceRegistry> {

    private final ServiceRegistry serviceRegistry;

    @Inject
    public ServiceRegistryProvider(@Named("HibernateConfig") URL hibernateConfig) {
        serviceRegistry = new StandardServiceRegistryBuilder()
            .configure(hibernateConfig)
            .build();
    }

    @Override
    public ServiceRegistry get() {
        return serviceRegistry;
    }
}
