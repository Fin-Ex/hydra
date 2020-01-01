package net.sf.l2j.gameserver.handler.skillhandlers;


import net.sf.l2j.gameserver.model.ShotType;
import net.sf.finex.enums.ESkillTargetType;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.ai.type.AttackableAI;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.SiegeSummon;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * This Handles Disabler skills
 *
 * @author _drunk_
 */
public class Disablers implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.STUN,
		ESkillType.ROOT,
		ESkillType.SLEEP,
		ESkillType.CONFUSION,
		ESkillType.AGGDAMAGE,
		ESkillType.AGGREDUCE,
		ESkillType.AGGREDUCE_CHAR,
		ESkillType.AGGREMOVE,
		ESkillType.MUTE,
		ESkillType.FAKE_DEATH,
		ESkillType.NEGATE,
		ESkillType.CANCEL_DEBUFF,
		ESkillType.PARALYZE,
		ESkillType.ERASE,
		ESkillType.BETRAY
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		ESkillType type = skill.getSkillType();

		final boolean ss = activeChar.isChargedShot(ShotType.SOULSHOT);
		final boolean sps = activeChar.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOT);

		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			Creature target = (Creature) obj;
			if (target.isDead() || (target.isInvul() && !target.isParalyzed())) // bypass if target is dead or invul (excluding invul from Petrification)
			{
				continue;
			}

			byte shld = Formulas.calcShldUse(activeChar, target, skill);

			switch (type) {
				case BETRAY:
					if (Formulas.calcSkillSuccess(activeChar, target, skill, shld, bsps)) {
						skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
					} else {
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill));
					}
					break;

				case FAKE_DEATH:
					// stun/fakedeath is not mdef dependant, it depends on lvl difference, target CON and power of stun
					skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
					break;

				case ROOT:
				case STUN:
					if (Formulas.calcSkillReflect(target, skill) == Formulas.SKILL_REFLECT_SUCCEED) {
						target = activeChar;
					}

					if (Formulas.calcSkillSuccess(activeChar, target, skill, shld, bsps)) {
						skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
					} else {
						if (activeChar instanceof Player) {
							activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill.getId()));
						}
					}
					break;

				case SLEEP:
				case PARALYZE: // use same as root for now
					if (Formulas.calcSkillReflect(target, skill) == Formulas.SKILL_REFLECT_SUCCEED) {
						target = activeChar;
					}

					if (Formulas.calcSkillSuccess(activeChar, target, skill, shld, bsps)) {
						skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
					} else {
						if (activeChar instanceof Player) {
							activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill.getId()));
						}
					}
					break;

				case MUTE:
					if (Formulas.calcSkillReflect(target, skill) == Formulas.SKILL_REFLECT_SUCCEED) {
						target = activeChar;
					}

					if (Formulas.calcSkillSuccess(activeChar, target, skill, shld, bsps)) {
						// stop same type effect if available
						L2Effect[] effects = target.getAllEffects();
						for (L2Effect e : effects) {
							if (e.getSkill().getSkillType() == type) {
								e.exit();
							}
						}
						skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
					} else {
						if (activeChar instanceof Player) {
							activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill.getId()));
						}
					}
					break;

				case CONFUSION:
					// do nothing if not on mob
					if (target instanceof Attackable) {
						if (Formulas.calcSkillSuccess(activeChar, target, skill, shld, bsps)) {
							L2Effect[] effects = target.getAllEffects();
							for (L2Effect e : effects) {
								if (e.getSkill().getSkillType() == type) {
									e.exit();
								}
							}
							skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
						} else {
							if (activeChar instanceof Player) {
								activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill));
							}
						}
					} else {
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.TARGET_IS_INCORRECT));
					}
					break;

				case AGGDAMAGE:
					if (target instanceof Attackable) {
						target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, (int) ((150 * skill.getPower()) / (target.getLevel() + 7)));
					}

					skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
					break;

				case AGGREDUCE:
					// these skills needs to be rechecked
					if (target instanceof Attackable) {
						skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));

						double aggdiff = ((Attackable) target).getHating(activeChar) - target.calcStat(Stats.Aggression, ((Attackable) target).getHating(activeChar), target, skill);

						if (skill.getPower() > 0) {
							((Attackable) target).reduceHate(null, (int) skill.getPower());
						} else if (aggdiff > 0) {
							((Attackable) target).reduceHate(null, (int) aggdiff);
						}
					}
					break;

				case AGGREDUCE_CHAR:
					// these skills needs to be rechecked
					if (Formulas.calcSkillSuccess(activeChar, target, skill, shld, bsps)) {
						if (target instanceof Attackable) {
							Attackable targ = (Attackable) target;
							targ.stopHating(activeChar);
							if (targ.getMostHated() == null && targ.hasAI() && targ.getAI() instanceof AttackableAI) {
								((AttackableAI) targ.getAI()).setGlobalAggro(-25);
								targ.getAggroList().clear();
								targ.getAI().setIntention(CtrlIntention.ACTIVE);
								targ.setWalking();
							}
						}
						skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
					} else {
						if (activeChar instanceof Player) {
							activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill));
						}

						target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar);
					}
					break;

				case AGGREMOVE:
					// these skills needs to be rechecked
					if (target instanceof Attackable && !target.isRaid()) {
						if (Formulas.calcSkillSuccess(activeChar, target, skill, shld, bsps)) {
							if (skill.getTargetType() == ESkillTargetType.TARGET_UNDEAD) {
								if (target.isUndead()) {
									((Attackable) target).reduceHate(null, ((Attackable) target).getHating(((Attackable) target).getMostHated()));
								}
							} else {
								((Attackable) target).reduceHate(null, ((Attackable) target).getHating(((Attackable) target).getMostHated()));
							}
						} else {
							if (activeChar instanceof Player) {
								activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill));
							}

							target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar);
						}
					} else {
						target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar);
					}
					break;

				case ERASE:
					// doesn't affect siege summons
					if (Formulas.calcSkillSuccess(activeChar, target, skill, shld, bsps) && !(target instanceof SiegeSummon)) {
						final Player summonOwner = ((Summon) target).getPlayer();
						final Summon summonPet = summonOwner.getActiveSummon();
						if (summonPet != null) {
							summonPet.unSummon(summonOwner);
							summonOwner.sendPacket(SystemMessageId.YOUR_SERVITOR_HAS_VANISHED);
						}
					} else {
						if (activeChar instanceof Player) {
							activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill));
						}
					}
					break;

				case CANCEL_DEBUFF:
					L2Effect[] effects = target.getAllEffects();

					if (effects == null || effects.length == 0) {
						break;
					}

					int count = (skill.getMaxNegatedEffects() > 0) ? 0 : -2;
					for (L2Effect e : effects) {
						if (e == null || !e.getSkill().isDebuff() || !e.getSkill().canBeDispeled()) {
							continue;
						}

						e.exit();

						if (count > -1) {
							count++;
							if (count >= skill.getMaxNegatedEffects()) {
								break;
							}
						}
					}
					break;

				case NEGATE:
					if (Formulas.calcSkillReflect(target, skill) == Formulas.SKILL_REFLECT_SUCCEED) {
						target = activeChar;
					}

					// Skills with negateId (skillId)
					if (skill.getNegateId().length != 0) {
						for (int id : skill.getNegateId()) {
							if (id != 0) {
								target.stopSkillEffects(id);
							}
						}
					} // All others negate type skills
					else {
						final int negateLvl = skill.getNegateLvl();

						for (L2Effect e : target.getAllEffects()) {
							final L2Skill effectSkill = e.getSkill();
							for (ESkillType skillType : skill.getNegateStats()) {
								// If power is -1 the effect is always removed without lvl check
								if (negateLvl == -1) {
									if (effectSkill.getSkillType() == skillType || (effectSkill.getEffectType() != null && effectSkill.getEffectType() == skillType)) {
										e.exit();
									}
								} // Remove the effect according to its power.
								else {
									if (effectSkill.getEffectType() != null && effectSkill.getEffectAbnormalLvl() >= 0) {
										if (effectSkill.getEffectType() == skillType && effectSkill.getEffectAbnormalLvl() <= negateLvl) {
											e.exit();
										}
									} else if (effectSkill.getSkillType() == skillType && effectSkill.getAbnormalLvl() <= negateLvl) {
										e.exit();
									}
								}
							}
						}
					}
					skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
					break;
			}
		}

		if (skill.hasSelfEffects()) {
			final L2Effect effect = activeChar.getFirstEffect(skill.getId());
			if (effect != null && effect.isSelfEffect()) {
				effect.exit();
			}

			skill.getEffectsSelf(activeChar);
		}
		activeChar.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
