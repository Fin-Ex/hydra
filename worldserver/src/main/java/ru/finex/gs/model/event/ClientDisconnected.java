package ru.finex.gs.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.gs.model.Client;

/**
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
public class ClientDisconnected {

    private final Client client;

}
