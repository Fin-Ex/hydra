package net.sf.l2j.gameserver.handler.skillhandlers;

import org.slf4j.LoggerFactory;

import net.sf.finex.handlers.dialog.DlgManager;
import net.sf.finex.handlers.dialog.requests.TeleportRequest;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ConfirmDlg;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @authors BiTi, Sami
 */
public class SummonFriend implements ISkillHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.SUMMON_FRIEND
	};

	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets) {
		if (!activeChar.isPlayer()) {
			return;
		}

		final Player player = (Player) activeChar;

		// Check player status.
		if (!Player.checkSummonerStatus(player)) {
			return;
		}

		for (WorldObject obj : targets) {
			// The target must be a player.
			if (!obj.isPlayer()) {
				continue;
			}

			// Can't summon yourself.
			final Player target = ((Player) obj);
			if (activeChar == target) {
				continue;
			}

			// Check target status.
			if (!Player.checkSummonTargetStatus(target, player)) {
				continue;
			}

			// Check target distance.
			if (MathUtil.checkIfInRange(50, activeChar, target, false)) {
				continue;
			}

			// Check target teleport request status.
			final TeleportRequest request = DlgManager.getInstance().getRequest(TeleportRequest.class);
			if (!request.handle(player, target, skill)) {
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ALREADY_SUMMONED).addCharName(target));
				continue;
			}

			// Send a request for Summon Friend skill.
			if (skill.getId() == 1403) {
				final ConfirmDlg confirm = new ConfirmDlg(SystemMessageId.S1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT.getId());
				confirm.addCharName(player);
				confirm.addZoneName(activeChar.getPosition());
				confirm.addTime(30000);
				confirm.addRequesterId(player.getObjectId());
				target.sendPacket(confirm);
			} else {
				Player.teleToTarget(target, player, skill);
				request.handle(player, target, null);
			}
		}
	}

	@Override
	public ESkillType[] getSkillIds() {
		return SKILL_IDS;
	}
}
