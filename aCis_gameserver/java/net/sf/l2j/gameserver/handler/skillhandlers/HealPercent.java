package net.sf.l2j.gameserver.handler.skillhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.SiegeFlag;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class HealPercent implements ISkillHandler {

	private static final ESkillType[] SKILL_IDS
			= {
				ESkillType.HEAL_PERCENT,
				ESkillType.MANAHEAL_PERCENT
			};

	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets) {
		// check for other effects
		ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(ESkillType.BUFF);
		if (handler != null) {
			handler.useSkill(activeChar, skill, targets);
		}

		boolean hp = false;
		boolean mp = false;

		switch (skill.getSkillType()) {
			case HEAL_PERCENT:
				hp = true;
				break;

			case MANAHEAL_PERCENT:
				mp = true;
				break;
		}

		StatusUpdate su = null;
		SystemMessage sm;
		double amount = 0;
		boolean full = skill.getPower() == 100.0;
		boolean targetPlayer = false;

		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (target.isDead() || target.isInvul()) {
				continue;
			}

			// Doors and flags can't be healed in any way
			if (target instanceof Door || target instanceof SiegeFlag) {
				continue;
			}

			targetPlayer = target instanceof Player;

			// Cursed weapon owner can't heal or be healed
			if (target != activeChar) {
				if (activeChar instanceof Player && ((Player) activeChar).isCursedWeaponEquipped()) {
					continue;
				}

				if (targetPlayer && ((Player) target).isCursedWeaponEquipped()) {
					continue;
				}
			}

			if (hp) {
				amount = Math.min(((full) ? target.getMaxHp() : target.getMaxHp() * skill.getPower() / 100.0), target.getMaxHp() - target.getCurrentHp());
				target.setCurrentHp(amount + target.getCurrentHp());
			} else if (mp) {
				amount = Math.min(((full) ? target.getMaxMp() : target.getMaxMp() * skill.getPower() / 100.0), target.getMaxMp() - target.getCurrentMp());
				target.setCurrentMp(amount + target.getCurrentMp());
			}

			if (targetPlayer) {
				su = new StatusUpdate(target);

				if (hp) {
					if (activeChar != target) {
						sm = SystemMessage.getSystemMessage(SystemMessageId.S2_HP_RESTORED_BY_S1).addCharName(activeChar);
					} else {
						sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED);
					}

					sm.addNumber((int) amount);
					target.sendPacket(sm);
					su.addAttribute(StatusUpdate.CUR_HP, (int) target.getCurrentHp());
				} else if (mp) {
					if (activeChar != target) {
						sm = SystemMessage.getSystemMessage(SystemMessageId.S2_MP_RESTORED_BY_S1).addCharName(activeChar);
					} else {
						sm = SystemMessage.getSystemMessage(SystemMessageId.S1_MP_RESTORED);
					}

					sm.addNumber((int) amount);
					target.sendPacket(sm);
					su.addAttribute(StatusUpdate.CUR_MP, (int) target.getCurrentMp());
				}

				target.sendPacket(su);
			}
		}
	}

	@Override
	public ESkillType[] getSkillIds() {
		return SKILL_IDS;
	}
}
