package ru.finex.auth.hydra.network.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.auth.hydra.network.GameSession;

/**
 * TODO m0nster.mind: move to core
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
public class SessionConnected implements SessionEvent {

    private GameSession session;

}
