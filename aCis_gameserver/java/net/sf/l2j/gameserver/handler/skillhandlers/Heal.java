package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.SiegeFlag;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class Heal implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.HEAL,
		ESkillType.HEAL_STATIC
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

		double power = skill.getPower() + activeChar.calcStat(Stats.GiveHP, 0, null, null);

		final boolean sps = activeChar.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOT);

		switch (skill.getSkillType()) {
			case HEAL_STATIC:
				break;

			default:
				double staticShotBonus = 0;
				int mAtkMul = 1; // mAtk multiplier

				if ((sps || bsps) && (activeChar.isPlayer() && activeChar.getPlayer().isMageClass()) || activeChar.isSummon()) {
					staticShotBonus = skill.getMpConsume(); // static bonus for spiritshots

					if (bsps) {
						mAtkMul = 4;
						staticShotBonus *= 2.4;
					} else {
						mAtkMul = 2;
					}
				} else if ((sps || bsps) && activeChar.isNpc()) {
					staticShotBonus = 2.4 * skill.getMpConsume(); // always blessed spiritshots
					mAtkMul = 4;
				} else {
					// shot dynamic bonus
					if (bsps) {
						mAtkMul *= 4;
					} else {
						mAtkMul += 1;
					}
				}

				power += staticShotBonus + Math.sqrt(mAtkMul * activeChar.getMAtk(activeChar, null));

				if (!skill.isPotion()) {
					activeChar.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
				}
		}

		double hp;
		for (WorldObject obj : targets) {
			if (!obj.isCreature()) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (target.isDead() || target.isInvul()) {
				continue;
			}

			if (target instanceof Door || target instanceof SiegeFlag) {
				continue;
			}

			// Player holding a cursed weapon can't be healed and can't heal
			if (target != activeChar) {
				if (target.isPlayer() && target.getPlayer().isCursedWeaponEquipped()) {
					continue;
				} else if (activeChar.isPlayer() && activeChar.getPlayer().isCursedWeaponEquipped()) {
					continue;
				}
			}

			switch (skill.getSkillType()) {
				case HEAL_PERCENT:
					if (target.isUndead() && target.isRaid()) {
						hp = activeChar.getMaxHp() * power / 100.0;
					} else {
						hp = target.getMaxHp() * power / 100.0;
					}
					break;
				default:
					hp = power;
					hp *= target.calcStat(Stats.GainHP, 100, null, null) / 100;
			}

			if (hp < 0) {
				hp = 0;
			}

			if (target.isUndead() && target.isAutoAttackable(activeChar)) {
				target.reduceCurrentHp(hp, activeChar, skill);
			} else {
				// If you have full HP and you get HP buff, u will receive 0HP restored message
				if ((target.getCurrentHp() + hp) >= target.getMaxHp()) {
					hp = target.getMaxHp() - target.getCurrentHp();
				}

				target.setCurrentHp(hp + target.getCurrentHp());
			}

			final StatusUpdate su = new StatusUpdate(target);
			su.addAttribute(StatusUpdate.CUR_HP, (int) target.getCurrentHp());
			target.sendPacket(su);

			if (target.isPlayer()) {
				if (skill.getId() == 4051) {
					target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.REJUVENATING_HP));
				} else {
					if (activeChar.isPlayer() && activeChar != target) {
						target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_HP_RESTORED_BY_S1).addCharName(activeChar).addNumber((int) hp));
					} else {
						target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED).addNumber((int) hp));
					}
				}
			}
		}
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
