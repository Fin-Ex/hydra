package ru.finex.ws.l2.service;

import lombok.RequiredArgsConstructor;
import ru.finex.ws.l2.model.entity.AvatarView;
import ru.finex.ws.l2.repository.AvatarRepository;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AvatarService {

    private final AvatarRepository repository;

    public List<AvatarView> getAvatars(String login) {
        return repository.findByLogin(login);
    }

}
