package ru.finex.ws.l2.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpeedComponent extends AbstractComponent {

    private double walkSpeed;
    private double runSpeed;
    private double swimSpeed;
    private double flySpeed = 0;

    private double attackSpeed;
    private double castSpeed;

    private double animMoveSpeed = 1;
    private double animAttackSpeed = 1;
    
    public double getClientWalkSpeed() {
        return walkSpeed / animMoveSpeed;
    }
    
    public double getClientRunSpeed() {
        return runSpeed / animMoveSpeed;
    }
    
    public double getClientSwimSpeed() {
        return swimSpeed / animMoveSpeed;
    }

}
