package ru.finex.ws.l2.component.player;

import lombok.Data;
import ru.finex.ws.model.component.AbstractComponent;

/**
 * @author m0nster.mind
 */
@Data
public class SpeedComponent extends AbstractComponent {

    private double walkSpeed = 80;
    private double runSpeed = 115;
    private double swimSpeed = 50;
    private double flySpeed = 0;

    private double attackSpeed = 111;
    private double castSpeed = 222;

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
