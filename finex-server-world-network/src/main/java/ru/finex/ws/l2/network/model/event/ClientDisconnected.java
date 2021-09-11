package ru.finex.ws.l2.network.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.ws.model.Client;

/**
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
public class ClientDisconnected {

    private final Client client;

}
