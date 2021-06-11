package ru.finex.core.db;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class ServiceRegistryProvider implements Provider<ServiceRegistry> {

    private final ServiceRegistry serviceRegistry;

    public ServiceRegistryProvider() {
        serviceRegistry = new StandardServiceRegistryBuilder()
            .configure(getConfigURL())
            .build();
    }

    private URL getConfigURL() {
        File file = new File("resources/hibernate.xml");
        if (file.exists()) {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return getClass().getClassLoader().getResource("hibernate.xml");
    }

    @Override
    public ServiceRegistry get() {
        return serviceRegistry;
    }
}
