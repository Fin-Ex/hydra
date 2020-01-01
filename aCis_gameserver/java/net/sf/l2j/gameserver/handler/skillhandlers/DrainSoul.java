package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author _drunk_
 */
public class DrainSoul implements IHandler {

	private static final String qn = "Q350_EnhanceYourWeapon";

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.DRAIN_SOUL
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		// Check player.
		if (activeChar == null || activeChar.isDead() || !(activeChar instanceof Player)) {
			return;
		}

		// Check quest condition.
		final Player player = (Player) activeChar;
		QuestState st = player.getQuestState(qn);
		if (st == null || !st.isStarted()) {
			return;
		}

		// Get target.
		WorldObject target = targets[0];
		if (target == null || !(target instanceof Attackable)) {
			return;
		}

		// Check monster.
		final Attackable mob = (Attackable) target;
		if (mob.isDead()) {
			return;
		}

		// Range condition, cannot be higher than skill's effectRange.
		if (!player.isInsideRadius(mob, skill.getEffectRange(), true, true)) {
			return;
		}

		// Register.
		mob.registerAbsorber(player);
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
