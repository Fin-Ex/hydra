package ru.finex.ws.hydra.component.prototype;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.prototype.ComponentPrototype;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
public class StatPrototype implements ComponentPrototype {

    private int pAtk;
    private int mAtk;

    private int pDef;
    private int mDef;

    private int criticalRate;
    private int magicCriticalRate;

    private int accuracy;
    private int magicAccuracy;

    private int evasion;
    private int magicEvasion;

}
