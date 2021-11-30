package ru.finex.ws.l2.network.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.ws.model.ClientSession;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDisconnected implements ClientEvent {

    private ClientSession client;

    @Override
    public void clear() {
        client = null;
    }
}
