package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.finex.handlers.talents.AutumnLeafs;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.SiegeFlag;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class HealPercent implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.HEAL_PERCENT,
		ESkillType.MANAHEAL_PERCENT
	};

	@Override
	public void invoke(Object... args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		
		// check for other effects
		final IHandler handler = HandlerTable.getInstance().get(Continuous.class);
		if (handler != null) {
			handler.invoke(activeChar, skill, targets);
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

			targetPlayer = target.isPlayer();

			// Cursed weapon owner can't heal or be healed
			if (target != activeChar) {
				if (activeChar.isPlayer() && activeChar.getPlayer().isCursedWeaponEquipped()) {
					continue;
				}

				if (targetPlayer && target.getPlayer().isCursedWeaponEquipped()) {
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
			
			if(activeChar.isPlayer() && AutumnLeafs.validate(activeChar.getPlayer(), skill)) {
				SkillTable.FrequentTalent.AUTUMN_LEAFS.getHandler().invoke(activeChar.getPlayer());
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
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
