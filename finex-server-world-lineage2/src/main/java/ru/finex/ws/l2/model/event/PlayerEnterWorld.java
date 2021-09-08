package ru.finex.ws.l2.model.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.finex.core.model.GameObject;

/**
 * @author m0nster.mind
 */
@Data
@RequiredArgsConstructor
public class PlayerEnterWorld {

    private final GameObject gameObject;

}
