package ru.finex.gs.inject.module.loader;

import com.google.inject.AbstractModule;
import org.hibernate.Session;
import ru.finex.gs.db.DbSessionServiceImpl;
import ru.finex.gs.db.HibernateSessionProvider;
import ru.finex.gs.service.DbSessionService;

/**
 * @author m0nster.mind
 */
public class DbModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DbSessionService.class).to(DbSessionServiceImpl.class);
        bind(Session.class).toProvider(HibernateSessionProvider.class);
    }

}
