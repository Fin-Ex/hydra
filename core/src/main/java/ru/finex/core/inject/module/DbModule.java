package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import org.hibernate.Session;
import ru.finex.core.db.DbSessionServiceImpl;
import ru.finex.core.db.HibernateSessionProvider;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.service.DbSessionService;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class DbModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DbSessionService.class).to(DbSessionServiceImpl.class);
        bind(Session.class).toProvider(HibernateSessionProvider.class);
    }

}
