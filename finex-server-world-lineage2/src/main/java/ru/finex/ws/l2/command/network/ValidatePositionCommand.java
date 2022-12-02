package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.component.ComponentService;
import ru.finex.ws.l2.component.base.CoordinateComponent;
import ru.finex.ws.l2.network.model.dto.ValidateLocationDto;
import ru.finex.ws.l2.network.session.GameClient;

import javax.inject.Inject;

@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ValidatePositionCommand extends AbstractNetworkCommand {

	@ToString.Include
	private final ValidateLocationDto dto;
	private final GameClient session;
	private final ComponentService componentService;

	@Override
	public void executeCommand() {
		componentService.getComponent(session.getGameObject(), CoordinateComponent.class)
			.validatePosition(session, dto);
	}
}
