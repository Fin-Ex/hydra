package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.ai.NextAction;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public final class RequestMagicSkillUse extends L2GameClientPacket {

	private int _magicId;
	protected boolean _ctrlPressed;
	protected boolean _shiftPressed;

	@Override
	protected void readImpl() {
		_magicId = readD(); // Identifier of the used skill
		_ctrlPressed = readD() != 0; // True if it's a ForceAttack : Ctrl pressed
		_shiftPressed = readC() != 0; // True if Shift pressed
	}

	@Override
	protected void runImpl() {
		// Get the current player
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		// Get the level of the used skill
		final int level = activeChar.getSkillLevel(_magicId);
		if (level <= 0) {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		// Get the L2Skill template corresponding to the skillID received from the client
		final L2Skill skill = SkillTable.getInstance().getInfo(_magicId, level);
		if (skill == null) {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			_log.warn("No skill found with id " + _magicId + " and level " + level + ".");
			return;
		}

		// If Alternate rule Karma punishment is set to true, forbid skill Return to player with Karma
		if (skill.getSkillType() == ESkillType.RECALL && !Config.KARMA_PLAYER_CAN_TELEPORT && activeChar.getKarma() > 0) {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		// players mounted on pets cannot use any toggle skills
		if (skill.isToggle() && activeChar.isMounted()) {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (activeChar.isOutOfControl()) {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (activeChar.isAttackingNow()) {
			activeChar.getAI().setNextAction(new NextAction(CtrlEvent.EVT_READY_TO_ACT, CtrlIntention.CAST, new Runnable() {
				@Override
				public void run() {
					activeChar.useMagic(skill, _ctrlPressed, _shiftPressed);
				}
			}));
		} else {
			activeChar.useMagic(skill, _ctrlPressed, _shiftPressed);
		}
	}
}
