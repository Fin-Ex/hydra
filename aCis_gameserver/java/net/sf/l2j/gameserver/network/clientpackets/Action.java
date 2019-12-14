package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;

public final class Action extends L2GameClientPacket {

	private int _objectId;
	@SuppressWarnings("unused")
	private int _originX, _originY, _originZ;
	private int _actionId;

	@Override
	protected void readImpl() {
		_objectId = readD();
		_originX = readD();
		_originY = readD();
		_originZ = readD();
		_actionId = readC();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (activeChar.isInObserverMode()) {
			activeChar.sendPacket(SystemMessageId.OBSERVERS_CANNOT_PARTICIPATE);
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (activeChar.getActiveRequester() != null || activeChar.isOutOfControl()) {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		final WorldObject obj = (activeChar.getTargetId() == _objectId) ? activeChar.getTarget() : World.getInstance().getObject(_objectId);
		if (obj == null) {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		switch (_actionId) {
			case 0:
				obj.onAction(activeChar);
				break;

			case 1:
				obj.onActionShift(activeChar);
				break;

			default:
				// Invalid action detected (probably client cheating), log this
				_log.warn(activeChar.getName() + " requested invalid action: " + _actionId);
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				break;
		}
	}

	@Override
	protected boolean triggersOnActionRequest() {
		return false;
	}
}
