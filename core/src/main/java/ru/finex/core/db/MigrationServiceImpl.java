package ru.finex.core.db;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.hibernate.boot.MetadataSources;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.Configurable;
import ru.finex.core.service.MigrationService;
import ru.finex.core.utils.Classes;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class MigrationServiceImpl implements MigrationService {

    private final Flyway flyway;
    private final DataSource dataSource;

    @Inject
    public MigrationServiceImpl(ServiceRegistry serviceRegistry) {
        MetadataSources metaSrc = new MetadataSources(serviceRegistry);
        Configuration configuration = new Configuration(metaSrc);
        Class<?> providerClass = Classes.getClass(configuration.getProperties().getProperty("hibernate.connection.provider_class"));
        ConnectionProvider connectionProvider = (ConnectionProvider) Classes.createInstance(providerClass);
        if (connectionProvider instanceof Configurable) {
            ((Configurable) connectionProvider).configure(configuration.getProperties());
        }

        dataSource = connectionProvider.unwrap(DataSource.class);
        flyway = Flyway.configure()
            .locations("evolution")
            .dataSource(dataSource)
            .load();

    }

    @Override
    public void migrateToLastVersion() {
        MigrateResult migrate = flyway.migrate();
        log.info("Executed migrations: {}", migrate.migrationsExecuted);
        if (!migrate.warnings.isEmpty()) {
            migrate.warnings.forEach(log::warn);
        }
    }

    @Override
    public void doneMigration() {
        if (dataSource instanceof Closeable) {
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
