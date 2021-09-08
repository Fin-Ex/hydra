package ru.finex.ws.l2.avatar;

import com.google.inject.ImplementedBy;
import ru.finex.ws.l2.model.dto.LobbyAvatarDto;

import java.util.List;

/**
 * @author m0nster.mind
 */
@ImplementedBy(AvatarServiceImpl.class)
public interface AvatarService {

    List<LobbyAvatarDto> getAvatars(String login);

}
