package ru.finex.ws.hydra.component.prototype;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.prototype.ComponentPrototype;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
public class SpeedPrototype implements ComponentPrototype {

    private double walkSpeed = 80;
    private double runSpeed = 115;
    private double swimSpeed = 50;
    private double flySpeed = 0;

    private double attackSpeed = 111;
    private double castSpeed = 222;

    private double animMoveSpeed = 1;
    private double animAttackSpeed = 1;

}
