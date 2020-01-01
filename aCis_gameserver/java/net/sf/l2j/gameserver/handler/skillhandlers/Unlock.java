package net.sf.l2j.gameserver.handler.skillhandlers;


import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.Chest;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class Unlock implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.UNLOCK,
		ESkillType.UNLOCK_SPECIAL
	};

	@Override
	public void invoke(Object... args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		final WorldObject object = targets[0];

		if (object instanceof Door) {
			final Door door = (Door) object;
			if (!door.isUnlockable() && skill.getSkillType() != ESkillType.UNLOCK_SPECIAL) {
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.UNABLE_TO_UNLOCK_DOOR));
				return;
			}

			if (doorUnlock(skill) && (!door.isOpened())) {
				door.openMe();
			} else {
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_UNLOCK_DOOR));
			}
		} else if (object instanceof Chest) {
			final Chest chest = (Chest) object;
			if (chest.isDead() || chest.isInteracted()) {
				return;
			}

			chest.setInteracted();
			if (chestUnlock(skill, chest)) {
				chest.setSpecialDrop();
				chest.doDie(null);
			} else {
				chest.addDamageHate(activeChar, 0, 999);
				chest.getAI().setIntention(CtrlIntention.ATTACK, activeChar);
			}
		} else {
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INCORRECT_TARGET));
		}
	}

	private static boolean doorUnlock(L2Skill skill) {
		if (skill.getSkillType() == ESkillType.UNLOCK_SPECIAL) {
			return Rnd.get(100) < skill.getPower();
		}

		switch (skill.getLevel()) {
			case 0:
				return false;
			case 1:
				return Rnd.get(120) < 30;
			case 2:
				return Rnd.get(120) < 50;
			case 3:
				return Rnd.get(120) < 75;
			default:
				return Rnd.get(120) < 100;
		}
	}

	private static boolean chestUnlock(L2Skill skill, Creature chest) {
		int chance = 0;
		if (chest.getLevel() > 60) {
			if (skill.getLevel() < 10) {
				return false;
			}

			chance = (skill.getLevel() - 10) * 5 + 30;
		} else if (chest.getLevel() > 40) {
			if (skill.getLevel() < 6) {
				return false;
			}

			chance = (skill.getLevel() - 6) * 5 + 10;
		} else if (chest.getLevel() > 30) {
			if (skill.getLevel() < 3) {
				return false;
			}

			if (skill.getLevel() > 12) {
				return true;
			}

			chance = (skill.getLevel() - 3) * 5 + 30;
		} else {
			if (skill.getLevel() > 10) {
				return true;
			}

			chance = skill.getLevel() * 5 + 35;
		}

		chance = Math.min(chance, 50);
		return Rnd.get(100) < chance;
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
