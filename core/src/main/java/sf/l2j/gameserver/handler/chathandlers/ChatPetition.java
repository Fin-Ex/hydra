package sf.l2j.gameserver.handler.chathandlers;

import sf.l2j.gameserver.handler.IHandler;
import sf.l2j.gameserver.instancemanager.PetitionManager;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.SystemMessageId;

public class ChatPetition implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		6,
		7
	};

	@Override
	public void invoke(Object... args) {
		final int type = (int) args[0];
		final Player activeChar = (Player) args[1];
		final String params = (String) args[2];
		final String text = (String) args[3];
		if (!PetitionManager.getInstance().isPlayerInConsultation(activeChar)) {
			activeChar.sendPacket(SystemMessageId.YOU_ARE_NOT_IN_PETITION_CHAT);
			return;
		}

		PetitionManager.getInstance().sendActivePetitionMessage(activeChar, text);
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
