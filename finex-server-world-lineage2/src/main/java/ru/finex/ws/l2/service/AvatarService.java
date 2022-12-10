package ru.finex.ws.l2.service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import ru.finex.core.component.ComponentService;
import ru.finex.core.object.GameObject;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.ws.l2.component.player.PlayerComponent;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.model.Gender;
import ru.finex.ws.l2.model.PlayerAppearanceClass;
import ru.finex.ws.l2.model.entity.AvatarPrototypeView;
import ru.finex.ws.l2.model.entity.AvatarView;
import ru.finex.ws.l2.model.entity.GameObjectEntity;
import ru.finex.ws.l2.model.entity.PlayerComponentEntity;
import ru.finex.ws.l2.model.exception.AppearanceClassNotFoundException;
import ru.finex.ws.l2.network.model.dto.CharacterCreateDto;
import ru.finex.ws.l2.network.model.dto.RequestCharacterNameCreatableDto;
import ru.finex.ws.l2.repository.AvatarRepository;
import ru.finex.ws.l2.repository.GameObjectRepository;
import ru.finex.ws.l2.service.component.PlayerService;
import ru.finex.ws.service.GameObjectService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final ComponentService componentService;
    private final PlayerService playerService;

    public List<AvatarView> get(String login) {
        return avatarRepository.findByLogin(login);
    }

    @ValidateOnExecution
    public void create(@Valid CharacterCreateDto dto, String login) throws ValidationException, AppearanceClassNotFoundException {
        String prototypeName = getPrototypeName(ClassId.ofId(dto.getClassId()), dto.getGender());

        int persistenceId = createGameObject();
        GameObject gameObject = gameObjectService.createGameObject(prototypeName, persistenceId);
        savePlayerSelection(gameObject, dto, login);
        persistenceService.persist(gameObject);

        log.debug("New avatar {} created!", gameObject);
    }

    @ValidateOnExecution
    public boolean isCreatable(@Valid RequestCharacterNameCreatableDto dto) throws ValidationException {
        return !playerService.isExists(dto.getName());
    }

    private int createGameObject() {
        GameObjectEntity entity = gameObjectRepository.create(new GameObjectEntity());
        return entity.getPersistenceId();
    }

    private void savePlayerSelection(GameObject gameObject, CharacterCreateDto dto, String login) throws AppearanceClassNotFoundException {
        PlayerComponentEntity entity = componentService.getComponent(gameObject, PlayerComponent.class)
            .getEntity();

        entity.setLogin(login);
        entity.setName(dto.getName());
        entity.setAppearanceClass(PlayerAppearanceClass.ofClassId(dto.getClassId(), dto.getRace(), dto.getGender()));
        entity.setGender(dto.getGender());
        entity.setRace(dto.getRace());
        entity.setFaceType(dto.getFace());
        entity.setHairColor(dto.getHairColor());
        entity.setHairType(dto.getHairType());
    }

    public List<AvatarPrototypeView> getPrototypes() {
        return StreamEx.of(ClassId.values())
            .filter(e -> e.getParent() == null) // only starter classes
            .map(this::getPrototype)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .distinct(AvatarPrototypeView::getClassId) // remove duplicates
            .collect(Collectors.toList());
    }

    private Optional<AvatarPrototypeView> getPrototype(ClassId classId) {
        return getPrototype(getPrototypeName(classId, null));
    }

    private Optional<AvatarPrototypeView> getPrototype(String name) {
        return avatarRepository.findPrototypeByName(name);
    }

    public String getPrototypeName(ClassId classId, Gender gender) {
        String genderPostfix = Optional.ofNullable(gender)
            .map(e -> "_" + e.name().toLowerCase())
            .orElse(StringUtils.EMPTY);

        return "player_" + classId.name().toLowerCase() + genderPostfix;
    }

}
