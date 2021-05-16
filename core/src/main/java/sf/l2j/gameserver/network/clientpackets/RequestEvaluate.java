package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.World;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.network.serverpackets.UserInfo;

public final class RequestEvaluate extends L2GameClientPacket {

	private int _targetId;

	@Override
	protected void readImpl() {
		_targetId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final Player target = World.getInstance().getPlayer(_targetId);
		if (target == null) {
			activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
			return;
		}

		// Exploit
		if (activeChar.getTarget() != target) {
			return;
		}

		if (activeChar.getLevel() < 10) {
			activeChar.sendPacket(SystemMessageId.ONLY_LEVEL_SUP_10_CAN_RECOMMEND);
			return;
		}

		if (activeChar.getRecomLeft() <= 0) {
			activeChar.sendPacket(SystemMessageId.NO_MORE_RECOMMENDATIONS_TO_HAVE);
			return;
		}

		if (activeChar.equals(target)) {
			activeChar.sendPacket(SystemMessageId.YOU_CANNOT_RECOMMEND_YOURSELF);
			return;
		}

		if (target.getRecomHave() >= 255) {
			activeChar.sendPacket(SystemMessageId.YOUR_TARGET_NO_LONGER_RECEIVE_A_RECOMMENDATION);
			return;
		}

		if (!activeChar.canRecom(target)) {
			activeChar.sendPacket(SystemMessageId.THAT_CHARACTER_IS_RECOMMENDED);
			return;
		}

		activeChar.giveRecom(target);
		activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_RECOMMENDED_S1_YOU_HAVE_S2_RECOMMENDATIONS_LEFT).addCharName(target).addNumber(activeChar.getRecomLeft()));
		target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_BEEN_RECOMMENDED_BY_S1).addCharName(activeChar));

		activeChar.sendPacket(new UserInfo(activeChar));
		target.broadcastUserInfo();
	}
}
