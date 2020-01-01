package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author _drunk_
 */
public class TakeCastle implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.TAKECASTLE
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (activeChar == null || !(activeChar instanceof Player)) {
			return;
		}

		if (targets.length == 0) {
			return;
		}

		final Player player = (Player) activeChar;
		if (!player.isClanLeader()) {
			return;
		}

		final Castle castle = CastleManager.getInstance().getCastle(player);
		if (castle == null || !player.checkIfOkToCastSealOfRule(castle, true, skill, targets[0])) {
			return;
		}

		castle.engrave(player.getClan(), targets[0]);
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
