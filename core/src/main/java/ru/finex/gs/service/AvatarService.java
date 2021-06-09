package ru.finex.gs.service;

import com.google.inject.ImplementedBy;
import ru.finex.gs.model.dto.LobbyAvatarDto;
import ru.finex.gs.service.impl.AvatarServiceImpl;

import java.util.List;

/**
 * @author m0nster.mind
 */
@ImplementedBy(AvatarServiceImpl.class)
public interface AvatarService {

    List<LobbyAvatarDto> getAvatars(String login);

}
