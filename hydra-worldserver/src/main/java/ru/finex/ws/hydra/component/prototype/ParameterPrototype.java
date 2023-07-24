package ru.finex.ws.hydra.component.prototype;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.prototype.ComponentPrototype;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@JsonNaming(LowerCaseStrategy.class)
public class ParameterPrototype implements ComponentPrototype {

    private int STR;
    private int DEX;
    private int CON;
    private int INT;
    private int WIT;
    private int MEN;
    private int LUC;
    private int CHA;

}
