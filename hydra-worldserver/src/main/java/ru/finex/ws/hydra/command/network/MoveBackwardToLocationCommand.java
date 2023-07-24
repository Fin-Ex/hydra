package ru.finex.ws.hydra.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.object.GameObject;
import ru.finex.ws.hydra.network.model.dto.MoveBackwardToLocationDto;
import ru.finex.ws.hydra.network.session.GameClient;
import ru.finex.ws.hydra.service.component.CoordinateService;

import javax.inject.Inject;

/**
 * @author finfan
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MoveBackwardToLocationCommand extends AbstractNetworkCommand {

	@ToString.Include
	private final MoveBackwardToLocationDto dto;
	private final GameClient session;

	private final CoordinateService coordinateService;

	@Override
	public void executeCommand() {
		GameObject gameObject = session.getGameObject();
		coordinateService.move(gameObject, dto.getDestination());
	}
}
