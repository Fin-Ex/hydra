package ru.finex.ws.hydra.component.prototype;

import lombok.Data;
import ru.finex.core.prototype.ComponentPrototype;

/**
 * @author m0nster.mind
 */
@Data
public class StatePrototype implements ComponentPrototype {

    private boolean isRunning;
    private boolean isSitting;

}
