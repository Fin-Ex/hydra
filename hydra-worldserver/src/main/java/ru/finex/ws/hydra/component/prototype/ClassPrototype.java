package ru.finex.ws.hydra.component.prototype;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.prototype.ComponentPrototype;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
public class ClassPrototype implements ComponentPrototype {

    private int classId;

}
