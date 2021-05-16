package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.Config;
import sf.l2j.gameserver.instancemanager.FishingChampionshipManager;
import sf.l2j.gameserver.model.actor.Player;

/**
 * Format: (ch)
 *
 * @author -Wooden-
 */
public final class RequestExFishRanking extends L2GameClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED) {
			FishingChampionshipManager.getInstance().showMidResult(activeChar);
		}
	}
}
