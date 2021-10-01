package ru.finex.ws.l2.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.finex.core.model.GameObject;
import ru.finex.core.model.GameObjectEvent;
import ru.finex.ws.l2.network.model.event.ClientEvent;
import ru.finex.ws.model.Client;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEnterWorld implements GameObjectEvent {

    private GameObject gameObject;

    @Override
    public void clear() {
        gameObject = null;
    }

}
