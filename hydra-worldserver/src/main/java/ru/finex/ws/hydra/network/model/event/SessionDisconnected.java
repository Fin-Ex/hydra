package ru.finex.ws.hydra.network.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.ws.hydra.network.session.GameClient;

/**
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
public class SessionDisconnected implements GameSessionEvent {

    private GameClient session;

}
