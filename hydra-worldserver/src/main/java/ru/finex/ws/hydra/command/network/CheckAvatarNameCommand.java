package ru.finex.ws.hydra.command.network;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.hydra.model.enums.CharacterNameReason;
import ru.finex.ws.hydra.network.OutcomePacketBuilderService;
import ru.finex.ws.hydra.network.model.dto.RequestCharacterNameCreatableDto;
import ru.finex.ws.hydra.network.session.GameClient;
import ru.finex.ws.hydra.service.AvatarService;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(onConstructor_ = { @Inject})
public class CheckAvatarNameCommand extends AbstractNetworkCommand {

    @ToString.Include
    private final RequestCharacterNameCreatableDto dto;
    @ToString.Include
    private final GameClient session;

    private final AvatarService avatarService;
    private final OutcomePacketBuilderService packets;

    @Override
    public void executeCommand() {
        CharacterNameReason reason;
        try {
            if (!avatarService.isCreatable(dto)) {
                reason = CharacterNameReason.NAME_ALREADY_EXISTS;
            } else {
                reason = CharacterNameReason.OK;
            }
        } catch (ValidationException e) {
            reason = CharacterNameReason.INVALID_NAME;
        } catch (Exception e) {
            reason = CharacterNameReason.CANNOT_CREATE_SERVER;
        }

        session.sendPacket(packets.characterNameCreatable(reason));
    }
}
