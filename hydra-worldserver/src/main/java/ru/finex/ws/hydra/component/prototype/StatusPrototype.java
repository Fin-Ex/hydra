package ru.finex.ws.hydra.component.prototype;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.prototype.ComponentPrototype;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
public class StatusPrototype implements ComponentPrototype {

    private double hp;
    private double maxHp;
    private double mp;
    private double maxMp;
    private double cp;
    private double maxCp;

}
