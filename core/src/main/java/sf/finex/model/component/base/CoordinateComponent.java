package sf.finex.model.component.base;

import lombok.Getter;
import lombok.Setter;
import sf.finex.model.component.AbstractComponent;

/**
 * Базовый компонент, хранящий в себе координаты игрового объекта на карте мира.
 *
 * @author m0nster.mind
 */
public class CoordinateComponent extends AbstractComponent {

    @Getter @Setter
    private double x = 82698;

    @Getter @Setter
    private double y = 148638;

    @Getter @Setter
    private double z = -3473;

    @Getter @Setter
    private int heading;

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
