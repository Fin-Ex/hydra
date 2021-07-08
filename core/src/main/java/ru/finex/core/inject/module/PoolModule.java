package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.InjectedModule;
import ru.finex.core.pool.PoolServiceImpl;
import ru.finex.core.service.PoolService;

/**
 * @author m0nster.mind
 */
@InjectedModule
public class PoolModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PoolService.class).to(PoolServiceImpl.class);
    }

}
