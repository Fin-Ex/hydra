package ru.finex.ws.l2.command.network;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.l2.model.enums.CharacterCreateReason;
import ru.finex.ws.l2.model.exception.AppearanceClassNotFoundException;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.CharacterCreateOk;
import ru.finex.ws.l2.network.model.dto.CharacterCreateDto;
import ru.finex.ws.l2.network.session.GameClient;
import ru.finex.ws.l2.service.AvatarService;

import javax.inject.Inject;

@Slf4j
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CharacterCreateCommand extends AbstractNetworkCommand {

	@ToString.Include
	private final CharacterCreateDto dto;
	@ToString.Include
	private final GameClient session;

	private final AvatarService avatarService;
	private final OutcomePacketBuilderService packets;

	@Override
	public void executeCommand() {
		try {
			avatarService.create(dto, session.getLogin());
			session.sendPacket(CharacterCreateOk.INSTANCE);
		} catch (ValidationException | AppearanceClassNotFoundException e) {
			log.debug("Validation failed, DTO: {}", dto, e);
			session.sendPacket(packets.charCreateFail(CharacterCreateReason.REASON_CREATION_FAILED));
		} catch (Exception e) {
			log.error("Fail to create avatar '{}' for session: {}", dto, session, e);
			session.sendPacket(packets.charCreateFail(CharacterCreateReason.REASON_CREATION_FAILED));
		}
	}
}
