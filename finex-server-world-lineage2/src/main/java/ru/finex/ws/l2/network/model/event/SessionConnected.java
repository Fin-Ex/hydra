package ru.finex.ws.l2.network.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.ws.l2.network.session.GameClient;

/**
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
public class SessionConnected implements GameSessionEvent {

    private GameClient session;

}
