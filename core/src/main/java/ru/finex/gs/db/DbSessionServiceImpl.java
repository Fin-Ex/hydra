package ru.finex.gs.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import ru.finex.core.GlobalContext;
import ru.finex.gs.model.entity.Entity;
import ru.finex.gs.service.DbSessionService;

import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * @author m0nster.mind
 */
@Singleton
public class DbSessionServiceImpl implements DbSessionService {

    private final SessionFactory sessionFactory;

    @Inject
    public DbSessionServiceImpl(DataSource dataSource) {
        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "org.postgresql.Driver");
        settings.put(Environment.URL, "jdbc:postgresql://localhost:5432/test?useSSL=false");
        settings.put(Environment.USER, "postgres");
        settings.put(Environment.PASS, "2236");
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "create-drop");
        settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, "true");
        configuration.setProperties(settings);
        GlobalContext.reflections.getSubTypesOf(Entity.class).forEach(configuration::addAnnotatedClass);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public Session openSession() {
        return sessionFactory.openSession();
    }
}
