package sf.l2j.gameserver.skills.l2skills;

import sf.finex.enums.EDependType;
import sf.finex.enums.ESkillTargetType;
import sf.finex.model.creature.attack.DamageInfo;
import sf.l2j.Config;
import sf.l2j.gameserver.model.ShotType;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.instance.Cubic;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.StatsSet;

public class L2SkillDrain extends L2Skill {

	private final EDependType drainDepend;
	private final float absorbPercent;
	private final int absorbABS;

	public L2SkillDrain(StatsSet set) {
		super(set);

		absorbPercent = set.getFloat("absorbPart", 0.f);
		absorbABS = set.getInteger("absorbAbs", 0);
		drainDepend = set.getEnum("absorbDepend", EDependType.class, EDependType.None);
	}

	@Override
	public void useSkill(Creature activeChar, WorldObject[] targets) {
		if (activeChar.isAlikeDead()) {
			return;
		}

		final boolean sps = activeChar.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOT);

		for (WorldObject obj : targets) {
			if (!obj.isCreature()) {
				continue;
			}

			final Creature target = obj.getCreature();
			if (target.isInvul()) {
				continue;
			}

			final DamageInfo info = new DamageInfo();
			info.shieldResult = Formulas.calcShldUse(activeChar, target, this);
			int damage;
			switch (drainDepend) {
				case PhysicalDamage:
					info.isCrit = Formulas.calcCrit(activeChar.getCriticalHit(target, this));
					damage = (int) Formulas.calcPhysDam(activeChar, target, this, info, activeChar.isChargedShot(ShotType.SOULSHOT));
					break;

				default:
					info.isCrit = Formulas.calcMCrit(activeChar.getMCriticalHit(target, this));
					damage = (int) Formulas.calcMagicDam(activeChar, target, this, info.shieldResult, false, sps, bsps, info.isCrit);
					break;
			}

			if (damage > 0) {
				int drainValue;
				int targetsCP = (int) target.getCurrentCp();
				int targetsHP = (int) target.getCurrentHp();

				// Drain system is different for L2Playable and monsters.
				// When playables attack CP of enemies, monsters don't bother about it.
				if (targetsCP > 0) {
					drainValue = (damage <= targetsCP) ? 0 : damage - targetsCP;
				} else {
					drainValue = (damage > targetsHP) ? targetsHP : damage;
				}

				final double hpAdd = absorbABS + absorbPercent * drainValue;
				if (hpAdd > 0) {
					activeChar.setCurrentHp(activeChar.getCurrentHp() + hpAdd);
				}

				if (!target.isDead()) {
					Formulas.calcCastBreak(target, damage);
					activeChar.sendDamageMessage(target, damage, info.isCrit, info.isCrit, false, false);

					if (hasEffects()) {
						if ((Formulas.calcSkillReflect(target, this) & Formulas.SKILL_REFLECT_SUCCEED) > 0) {
							activeChar.stopSkillEffects(getId());
							getEffects(target, activeChar);
							activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(getId()));
						} else {
							target.stopSkillEffects(getId());
							if (Formulas.calcSkillSuccess(activeChar, target, this, info.shieldResult, bsps)) {
								getEffects(activeChar, target);
							} else {
								activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(getId()));
							}
						}
					}
					target.reduceCurrentHp(damage, activeChar, this);
				}
			}
		}

		if (hasSelfEffects()) {
			final L2Effect effect = activeChar.getFirstEffect(getId());
			if (effect != null && effect.isSelfEffect()) {
				effect.exit();
			}

			getEffectsSelf(activeChar);
		}

		activeChar.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, isStaticReuse());
	}

	public void useCubicSkill(Cubic activeCubic, WorldObject[] targets) {
		if (Config.DEBUG) {
			_log.info("L2SkillDrain: useCubicSkill()");
		}

		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (target.isAlikeDead() && getTargetType() != ESkillTargetType.TARGET_CORPSE_MOB) {
				continue;
			}

			final boolean mcrit = Formulas.calcMCrit(activeCubic.getMCriticalHit(target, this));
			final byte shld = Formulas.calcShldUse(activeCubic.getOwner(), target, this);
			final int damage = (int) Formulas.calcMagicDam(activeCubic, target, this, mcrit, shld);

			// Check to see if we should damage the target
			if (damage > 0) {
				final Player owner = activeCubic.getOwner();
				final double hpAdd = absorbABS + absorbPercent * damage;
				if (hpAdd > 0) {
					final double hp = ((owner.getCurrentHp() + hpAdd) > owner.getMaxHp() ? owner.getMaxHp() : (owner.getCurrentHp() + hpAdd));

					owner.setCurrentHp(hp);

					StatusUpdate suhp = new StatusUpdate(owner);
					suhp.addAttribute(StatusUpdate.CUR_HP, (int) hp);
					owner.sendPacket(suhp);
				}

				// That section is launched for drain skills made on ALIVE targets.
				if (!target.isDead() || getTargetType() != ESkillTargetType.TARGET_CORPSE_MOB) {
					target.reduceCurrentHp(damage, activeCubic.getOwner(), this);

					// Manage cast break of the target (calculating rate, sending message...)
					Formulas.calcCastBreak(target, damage);

					owner.sendDamageMessage(target, damage, mcrit, false, false, false);
				}
			}
		}
	}
}
