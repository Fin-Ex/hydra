package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.component.ComponentService;
import ru.finex.ws.l2.component.base.CoordinateComponent;
import ru.finex.ws.l2.component.player.ColliderComponent;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.MoveBackwardToLocationDto;
import ru.finex.ws.l2.network.session.GameClient;

import javax.inject.Inject;

@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MoveBackwardToLocationCommand extends AbstractNetworkCommand {

	@ToString.Include
	private final MoveBackwardToLocationDto dto;
	private final GameClient session;
	private final OutcomePacketBuilderService outcomePacketBuilderService;
	private final ComponentService componentService;

	@Override
	public void executeCommand() {
		CoordinateComponent coordinateComponent = componentService.getComponent(session.getGameObject(), CoordinateComponent.class);
		ColliderComponent collisionComponent = componentService.getComponent(session.getGameObject(), ColliderComponent.class);
		coordinateComponent.moveToLocation(session, dto, outcomePacketBuilderService, collisionComponent);
	}
}
