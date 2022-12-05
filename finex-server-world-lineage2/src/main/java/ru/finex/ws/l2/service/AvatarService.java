package ru.finex.ws.l2.service;

import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.object.GameObject;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.ws.l2.model.entity.AvatarView;
import ru.finex.ws.l2.model.entity.GameObjectEntity;
import ru.finex.ws.l2.model.enums.Race;
import ru.finex.ws.l2.network.model.dto.CharacterCreateDto;
import ru.finex.ws.l2.repository.AvatarRepository;
import ru.finex.ws.l2.repository.GameObjectRepository;
import ru.finex.ws.service.GameObjectService;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final GameObjectRepository gameObjectRepository;
    private final GameObjectService gameObjectService;
    private final GameObjectPersistenceService persistenceService;

    public List<AvatarView> getAvatars(String login) {
        return avatarRepository.findByLogin(login);
    }

    @ValidateOnExecution
    public void createAvatar(@Valid CharacterCreateDto dto) {
        if (dto.getSex() == 0 && dto.getHairStyle() > 4) {
            throw new IllegalArgumentException(String.format(
                "Character creation failure. Wrong hairStyle/sex: style=%d[sex=%d]",
                dto.getHairStyle(),
                dto.getSex()
            ));
        }

        if (dto.getRace() == Race.ERTHEIA.getId()) {
            throw new IllegalArgumentException(String.format(
                "Character creation failure. Wrong race: %d",
                dto.getRace()
            ));
        }

        int persistenceId = createGameObject();
        GameObject gameObject = gameObjectService.createGameObject("test", persistenceId);
        persistenceService.persist(gameObject);

        //initNewChar(session, newChar);
        log.debug("New character {} created!", gameObject);
    }

    private int createGameObject() {
        GameObjectEntity entity = gameObjectRepository.create(new GameObjectEntity());
        return entity.getPersistenceId();
    }

}
