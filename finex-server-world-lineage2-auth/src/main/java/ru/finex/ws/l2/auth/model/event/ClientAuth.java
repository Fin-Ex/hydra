package ru.finex.ws.l2.auth.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.gs.model.Client;

/**
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
public class ClientAuth {

    private final Client client;
    private final boolean isAuthed;

}
