package ru.finex.ws.hydra.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.hydra.network.model.dto.ValidateLocationDto;
import ru.finex.ws.hydra.network.session.GameClient;

import javax.inject.Inject;

/**
 * @author finfan
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ValidatePositionCommand extends AbstractNetworkCommand {

	@ToString.Include
	private final ValidateLocationDto dto;
	@ToString.Include
	private final GameClient session;

	@Override
	public void executeCommand() {

	}
}
