package ru.finex.auth.l2.service;

import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.model.entity.ServerDataEntity;
import ru.finex.auth.l2.repository.ServerDataRepository;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ServerService {

    private final ServerDataRepository repository;

    public List<ServerDataEntity> getAll() {
        return repository.findAll();
    }

}
