package ru.finex.auth.l2.service;

import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.model.dto.UserServerDto;
import ru.finex.auth.l2.model.entity.ServerDataEntity;
import ru.finex.auth.l2.populator.ServerDataPopulator;
import ru.finex.auth.l2.repository.ServerDataRepository;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ServerService {

    private final ServerDataRepository repository;
    private final ServerDataPopulator populator;

    public List<ServerDataEntity> getAll() {
        return repository.findAll();
    }

    public List<UserServerDto> getUserServerList(long userId) {
        return getAll().stream()
            .map(populator::toUserServerDto)
            .collect(Collectors.toList());
    }

}
