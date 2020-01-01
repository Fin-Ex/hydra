package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class Craft implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.COMMON_CRAFT,
		ESkillType.DWARVEN_CRAFT
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (activeChar == null || !(activeChar instanceof Player)) {
			return;
		}

		Player player = (Player) activeChar;
		if (player.isInStoreMode()) {
			player.sendPacket(SystemMessageId.CANNOT_CREATED_WHILE_ENGAGED_IN_TRADING);
			return;
		}
		player.requestBookOpen(skill.getSkillType() == ESkillType.DWARVEN_CRAFT);
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
