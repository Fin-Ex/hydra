package ru.finex.auth.l2.network.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.auth.l2.network.GameSession;

/**
 * TODO m0nster.mind: move to core
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
public class SessionDisconnected implements SessionEvent {

    private GameSession session;

}
