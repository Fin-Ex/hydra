package ru.finex.ws.l2.command.network;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.l2.model.enums.CharCreateFailReason;
import ru.finex.ws.l2.network.OutcomePacketBuilderService;
import ru.finex.ws.l2.network.model.dto.CharCreateOk;
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
			avatarService.createAvatar(dto);
		} catch (Exception e) {
			log.debug("", e);
			session.sendPacket(packets.charCreateFail(CharCreateFailReason.REASON_CREATION_FAILED));
			return;
		}

		session.sendPacket(CharCreateOk.INSTANCE);
		session.sendPacket(packets.charSelectInfo(session.getLogin(), session.getData().getSessionId()));
	}
}
