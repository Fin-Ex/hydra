package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.World;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;

public final class AttackRequest extends L2GameClientPacket {

	// cddddc
	private int _objectId;
	@SuppressWarnings("unused")
	private int _originX, _originY, _originZ;
	@SuppressWarnings("unused")
	private int _attackId;

	@Override
	protected void readImpl() {
		_objectId = readD();
		_originX = readD();
		_originY = readD();
		_originZ = readD();
		_attackId = readC(); // 0 for simple click 1 for shift-click
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

		// avoid using expensive operations if not needed
		final WorldObject target;
		if (activeChar.getTargetId() == _objectId) {
			target = activeChar.getTarget();
		} else {
			target = World.getInstance().getObject(_objectId);
		}

		if (target == null) {
			return;
		}

		if (activeChar.getTarget() != target) {
			target.onAction(activeChar);
		} else {
			if ((target.getObjectId() != activeChar.getObjectId()) && !activeChar.isInStoreMode() && activeChar.getActiveRequester() == null) {
				target.onForcedAttack(activeChar);
			} else {
				sendPacket(ActionFailed.STATIC_PACKET);
			}
		}
	}
}
