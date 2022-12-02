package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.l2.network.model.dto.CharacterCreateDto;
import ru.finex.ws.l2.service.CharacterCreateService;

import javax.inject.Inject;

@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CharacterCreateCommand extends AbstractNetworkCommand {

	@ToString.Include
	private final CharacterCreateDto dto;
	private final CharacterCreateService characterCreateService;

	@Override
	public void executeCommand() {
		characterCreateService.execute(dto);
	}
}
