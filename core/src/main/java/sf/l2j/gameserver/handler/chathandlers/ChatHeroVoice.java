package sf.l2j.gameserver.handler.chathandlers;

import sf.l2j.gameserver.handler.IHandler;
import sf.l2j.gameserver.model.World;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.FloodProtectors;
import sf.l2j.gameserver.network.FloodProtectors.Action;
import sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class ChatHeroVoice implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		17
	};

	@Override
	public void invoke(Object... args) {
		final int type = (int) args[0];
		final Player activeChar = (Player) args[1];
		final String params = (String) args[2];
		final String text = (String) args[3];
		if (!activeChar.isHero()) {
			return;
		}

		if (!FloodProtectors.performAction(activeChar.getClient(), Action.HERO_VOICE)) {
			return;
		}

		final CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
		for (Player player : World.getInstance().getPlayers()) {
			player.sendPacket(cs);
		}
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
