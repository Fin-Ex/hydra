package ru.finex.gs.service.impl;

import lombok.RequiredArgsConstructor;
import ru.finex.gs.model.dto.LobbyAvatarDto;
import ru.finex.gs.service.AvatarService;
import ru.finex.gs.service.persistence.ObjectPersistenceService;

import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AvatarServiceImpl implements AvatarService {

    private final ObjectPersistenceService persistenceService;

    @Override
    public List<LobbyAvatarDto> getAvatars(String login) {
        LobbyAvatarDto avatar = new LobbyAvatarDto();
        avatar.setPersistenceId(1);
        persistenceService.restore(avatar);

        return Collections.singletonList(avatar);
    }

}
