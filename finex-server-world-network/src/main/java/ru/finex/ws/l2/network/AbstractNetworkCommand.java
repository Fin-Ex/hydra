package ru.finex.ws.l2.network;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.ws.model.Client;
import ru.finex.ws.tick.TickPriority;

/**
 * @author m0nster.mind
 */
public abstract class AbstractNetworkCommand extends AbstractGameObjectCommand {

    @Getter @Setter
    private Client client;

    @Override
    public int getPriority() {
        return TickPriority.INPUT;
    }
}
