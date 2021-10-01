package ru.finex.ws.l2.auth.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.ws.l2.network.model.event.ClientEvent;
import ru.finex.ws.model.Client;

/**
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAuth implements ClientEvent {

    private Client client;
    private boolean isAuthed;

    @Override
    public void clear() {
        client = null;
        isAuthed = false;
    }
}
