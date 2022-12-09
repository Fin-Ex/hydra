package ru.finex.ws.l2.component.prototype;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.prototype.ComponentPrototype;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
public class ColliderPrototype implements ComponentPrototype {

    private double width;
    private double height;

}
