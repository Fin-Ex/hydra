package ru.finex.ws.hydra.command.network;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.hydra.network.model.dto.ExSendClientIniDto;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ExSendClientIniCommand extends AbstractNetworkCommand {

    private final ExSendClientIniDto dto;

    @Override
    public void executeCommand() {
        // TODO m0nster.mind: save ini file
    }

}
