package ru.finex.ws.hydra.component.prototype;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.ws.hydra.model.enums.Gender;
import ru.finex.ws.hydra.model.PlayerAppearanceClass;
import ru.finex.ws.hydra.model.enums.Race;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
public class PlayerPrototype implements ComponentPrototype {

    private Race race;
    private Gender gender;
    private PlayerAppearanceClass appearanceClass;
    private int hairType;
    private int hairColor;
    private int faceType;
    private int nameColor;
    private int titleColor;

}
