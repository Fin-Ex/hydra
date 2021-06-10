package ru.finex.gs.model.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.finex.gs.model.GameObject;

/**
 * @author m0nster.mind
 */
@Data
@RequiredArgsConstructor
public class PlayerEnterWorld {

    private final GameObject gameObject;

}
