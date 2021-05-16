package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.FloodProtectors;
import sf.l2j.gameserver.network.FloodProtectors.Action;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SocialAction;

public class RequestSocialAction extends L2GameClientPacket {

	private int _actionId;

	@Override
	protected void readImpl() {
		_actionId = readD();
	}

	@Override
	protected void runImpl() {
		if (!FloodProtectors.performAction(getClient(), Action.SOCIAL)) {
			return;
		}

		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (activeChar.isFishing()) {
			activeChar.sendPacket(SystemMessageId.CANNOT_DO_WHILE_FISHING_3);
			return;
		}

		if (_actionId < 2 || _actionId > 13) {
			return;
		}

		if (activeChar.isInStoreMode() || activeChar.getActiveRequester() != null || activeChar.isAlikeDead() || activeChar.getAI().getIntention() != CtrlIntention.IDLE) {
			return;
		}

		activeChar.broadcastPacket(new SocialAction(activeChar, _actionId));
	}
}
