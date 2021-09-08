package ru.finex.ws.l2.avatar;

import lombok.RequiredArgsConstructor;
import ru.finex.core.persistence.ObjectPersistenceService;
import ru.finex.ws.l2.model.dto.LobbyAvatarDto;

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
