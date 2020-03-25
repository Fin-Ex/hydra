/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.casting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.handlers.talents.RecoiledBlast;
import net.sf.finex.handlers.talents.SonicAssault;
import net.sf.finex.handlers.talents.WildHurricane;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.events.OnCast;
import net.sf.l2j.gameserver.model.holder.SkillUseHolder;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillLaunched;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.skills.ESkillType;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;
import net.sf.finex.model.talents.ITalentHandler;

/**
 *
 * @author FinFan
 */
@Slf4j
@Getter
@Setter
public final class Cast {

	private final Creature caster;
	private final L2Skill skill;
	private final boolean simultaneously;
	private final boolean effectWhileCasting;

	private Creature target;
	private WorldObject[] targets;
	private int hitTime;
	private int coolTime;
	private int reuseDelay;
	private long interruptTime;

	public Cast(Creature caster, L2Skill skill, boolean simultaneously) {
		this.caster = caster;
		this.skill = skill;
		this.hitTime = skill.getHitTime();
		this.coolTime = skill.getCoolTime();
		this.simultaneously = skill.isSimultaneousCast() && !simultaneously ? true : simultaneously;
		this.effectWhileCasting = skill.isChanneling();

		if (!effectWhileCasting) {
			hitTime = Formulas.calcAtkSpd(caster, skill, hitTime);
			if (coolTime > 0) {
				coolTime = Formulas.calcAtkSpd(caster, skill, coolTime);
			}
		}

		if (skill.isMagic() && !effectWhileCasting) {
			if (caster.isChargedShot(ShotType.SPIRITSHOT) || caster.isChargedShot(ShotType.BLESSED_SPIRITSHOT)) {
				hitTime = (int) (0.70 * hitTime);
				coolTime = (int) (0.70 * coolTime);
			}
		}

		if (skill.isStaticHitTime()) {
			hitTime = skill.getHitTime();
			coolTime = skill.getCoolTime();
		} else if (skill.getHitTime() >= 500 && hitTime < 500) {
			hitTime = 500;
		}

		this.interruptTime = System.currentTimeMillis() + hitTime / 2;
	}

	public void start() {
		try {
			if (!caster.checkDoCastConditions(skill)) {
				onFailure();
				return;
			}

			caster.stopEffectsOnAction();
			caster.rechargeShots(skill.useSoulShot(), skill.useSpiritShot());

			targets = skill.getTargetList(caster);
			boolean doit = false;
			switch (skill.getTargetType()) {
				case TARGET_AREA_SUMMON:
					target = caster.getPlayable().getActiveSummon();
					break;
				case TARGET_AURA:
				case TARGET_FRONT_AURA:
				case TARGET_BEHIND_AURA:
				case TARGET_AURA_UNDEAD:
				case TARGET_GROUND:
					target = caster;
					break;
				case TARGET_SELF:
				case TARGET_CORPSE_ALLY:
				case TARGET_PET:
				case TARGET_SUMMON:
				case TARGET_OWNER_PET:
				case TARGET_PARTY:
				case TARGET_CLAN:
				case TARGET_ALLY:
					doit = true;
				default:
					if (targets.length == 0) {
						onFailure();
						return;
					}

					switch (skill.getSkillType()) {
						case BUFF:
						case HEAL:
						case COMBATPOINTHEAL:
						case MANAHEAL:
						case SEED:
						case REFLECT:
							doit = true;
							break;
					}

					target = (doit) ? (Creature) targets[0] : (Creature) caster.getTarget();
			}

			if (target == null) {
				onFailure();
				return;
			}

			// Set the _castInterruptTime and casting status (Player already has this true)
			if (simultaneously) {
				// queue herbs and potions
				if (caster.isCastingSimultaneouslyNow()) {
					ThreadPool.schedule(new UsePotionTask(caster, skill), 100);
					return;
				}
				caster.setIsCastingSimultaneouslyNow(true);
				caster.setLastSimultaneousSkillCast(skill);
			} else {
				caster.setIsCastingNow(true);
				caster.setLastSkillCast(skill);
			}

			reuseDelay = skill.getReuseDelay(caster, targets == null ? 0 : targets.length);
			if (!skill.isStaticReuse()) {
				reuseDelay *= caster.calcStat(skill.isMagic() ? Stats.SpellReuse : Stats.SkillReuse, 1, null, null);
				reuseDelay /= (skill.isMagic() ? caster.getMAtkSpd() : caster.getPAtkSpd()) / 333.0;
			}

			boolean skillMastery = Formulas.calcSkillMastery(caster, skill);

			// Skill reuse check
			if (reuseDelay > 30000 && !skillMastery && !skill.isToggle()) {
				caster.addTimeStamp(skill, reuseDelay);
			}

			// Check if this skill consume mp on start casting
			int initmpcons = caster.getStat().getMpInitialConsume(skill);
			if (initmpcons > 0) {
				caster.getStatus().reduceMp(initmpcons);
				StatusUpdate su = new StatusUpdate(caster);
				su.addAttribute(StatusUpdate.CUR_MP, (int) caster.getCurrentMp());
				caster.sendPacket(su);
			}

			// Disable the skill during the re-use delay and create a task EnableSkill with Medium priority to enable it at the end of the re-use delay
			if (reuseDelay > 10) {
				if (skillMastery) {
					reuseDelay = 100;

					if (caster.getPlayer() != null) {
						caster.getPlayer().sendPacket(SystemMessageId.SKILL_READY_TO_USE_AGAIN);
					}
				}

				if (!skill.isToggle()) {
					caster.disableSkill(skill, reuseDelay);
				}
			}

			// Make sure that char is facing selected target
			if (target != caster) {
				caster.setHeading(MathUtil.calculateHeadingFrom(caster, target));
			}

			// For force buff skills, start the effect as long as the player is casting.
			if (effectWhileCasting) {
				// Consume Items if necessary and Send the Server->Client packet InventoryUpdate with Item modification to all the Creature
				if (skill.getItemConsumeId() > 0) {
					if (!caster.destroyItemByItemId("Consume", skill.getItemConsumeId(), skill.getItemConsume(), null, true)) {
						caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
						onFailure();
						return;
					}
				}

				if (skill.getSkillType() == ESkillType.FUSION) {
					caster.startFusionSkill(target, skill);
				} else {
					caster.callSkill(skill, targets);
				}
			}

			final int displayId = skill.getId();
			final int level = Math.max(skill.getLevel(), 1);

			if (!skill.isPotion()) {
				caster.broadcastPacket(new MagicSkillUse(caster, target, displayId, level, hitTime, !skill.isToggle() ? reuseDelay : 0, false));
				caster.broadcastPacket(new MagicSkillLaunched(caster, displayId, level, (targets == null || targets.length == 0) ? new WorldObject[]{
					target
				} : targets));
			} else {
				caster.broadcastPacket(new MagicSkillUse(caster, target, displayId, level, 0, 0));
			}

			if (caster.isPlayable()) {
				if (caster.isPlayer() && skill.getId() != 1312) {
					caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.USE_S1).addSkillName(skill));
				}

				if (!effectWhileCasting && skill.getItemConsumeId() > 0) {
					if (!caster.destroyItemByItemId("Consume", skill.getItemConsumeId(), skill.getItemConsume(), null, true)) {
						caster.getPlayer().sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
						caster.abortCast();
						return;
					}
				}

				// Before start AI Cast Broadcast Fly Effect is Need
				if (caster.isPlayer() && skill.getFlyType() != null) {
					ThreadPool.schedule(new FlyToLocationTask(caster, target, skill), 50);
				}
			}

			final MagicUseTask mut = new MagicUseTask(this);

			// launch the magic in hitTime milliseconds
			if (hitTime > 410) {
				// Send SetupGauge with the color of the gauge and the casting time
				if (caster.isPlayer() && !effectWhileCasting) {
					caster.sendPacket(new SetupGauge(SetupGauge.GaugeColor.BLUE, hitTime));
				}

				if (effectWhileCasting) {
					mut.phase = 2;
				}

				if (simultaneously) {
					Future<?> future = caster.getSkillCast2();
					if (future != null) {
						future.cancel(true);
						caster.setSkillCast2(null);
					}

					// Create a task MagicUseTask to launch the MagicSkill at the end of the casting time (hitTime)
					// For client animation reasons (party buffs especially) 400 ms before!
					caster.setSkillCast2(ThreadPool.schedule(mut, hitTime - 400));
				} else {
					Future<?> future = caster.getSkillCast();
					if (future != null) {
						future.cancel(true);
						caster.setSkillCast(null);
					}

					// Create a task MagicUseTask to launch the MagicSkill at the end of the casting time (hitTime)
					// For client animation reasons (party buffs especially) 400 ms before!
					caster.setSkillCast(ThreadPool.schedule(mut, hitTime - 400));
				}
			} else {
				hitTime = 0;
				launch(mut);
			}

			caster.getEventBus().notify(new OnCast(caster, target, skill, OnCast.CastType.START, targets));
		} catch (Exception e) {
			log.error("Error in start() skill({}) cast", skill.getId(), e);
		}
	}

	public void launch(MagicUseTask mut) {
		if (skill == null || targets == null) {
			caster.abortCast();
			return;
		}

		if (targets.length == 0) {
			switch (skill.getTargetType()) {
				// only AURA-type skills can be cast without target
				case TARGET_AURA:
				case TARGET_FRONT_AURA:
				case TARGET_BEHIND_AURA:
				case TARGET_AURA_UNDEAD:
					break;
				default:
					caster.abortCast();
					return;
			}
		}

		// Escaping from under skill's radius and peace zone check. First version, not perfect in AoE skills.
		int escapeRange = 0;
		if (skill.getEffectRange() > escapeRange) {
			escapeRange = skill.getEffectRange();
		} else if (skill.getCastRange() < 0 && skill.getSkillRadius() > 80) {
			escapeRange = skill.getSkillRadius();
		}

		if (targets.length > 0 && escapeRange > 0) {
			int _skiprange = 0;
			int _skipgeo = 0;
			int _skippeace = 0;
			List<Creature> targetList = new ArrayList<>(targets.length);
			for (WorldObject tgt : targets) {
				if (tgt instanceof Creature) {
					if (!MathUtil.checkIfInRange(escapeRange, caster, tgt, true)) {
						_skiprange++;
						continue;
					}

					if (skill.getSkillRadius() > 0 && skill.isOffensive() && !GeoEngine.getInstance().canSeeTarget(caster, tgt)) {
						_skipgeo++;
						continue;
					}

					if (skill.isOffensive() && Creature.isInsidePeaceZone(caster, tgt)) {
						_skippeace++;
						continue;
					}
					targetList.add((Creature) tgt);
				}
			}

			if (targetList.isEmpty()) {
				if (caster.isPlayer()) {
					if (_skiprange > 0) {
						caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DIST_TOO_FAR_CASTING_STOPPED));
					} else if (_skipgeo > 0) {
						caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANT_SEE_TARGET));
					} else if (_skippeace > 0) {
						caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.TARGET_IN_PEACEZONE));
					}
				}
				caster.abortCast();
				return;
			}
			targets = targetList.toArray(new Creature[targetList.size()]);
		}

		// Ensure that a cast is in progress
		// Check if player is using fake death.
		// Potions can be used while faking death.
		if ((simultaneously && !caster.isCastingSimultaneouslyNow()) || (!simultaneously && !caster.isCastingNow()) || (caster.isAlikeDead() && !skill.isPotion())) {
			// now cancels both, simultaneous and normal
			caster.getAI().notifyEvent(CtrlEvent.EVT_CANCEL);
			return;
		}

		mut.phase = 2;
		if (hitTime == 0) {
			hit(mut);
		} else {
			caster.setSkillCast(ThreadPool.schedule(mut, 400));
		}
	}

	public void hit(MagicUseTask mut) {
		if (skill == null || targets == null) {
			caster.abortCast();
			return;
		}

		if (caster.getFusionSkill() != null) {
			if (simultaneously) {
				caster.setSkillCast2(null);
				caster.setIsCastingSimultaneouslyNow(false);
			} else {
				caster.setSkillCast(null);
				caster.setIsCastingNow(false);
			}
			caster.getFusionSkill().onCastAbort();
			caster.notifyQuestEventSkillFinished(skill, targets[0]);
			return;
		}

		final L2Effect mog = caster.getFirstEffect(L2EffectType.SIGNET_GROUND);
		if (mog != null) {
			if (simultaneously) {
				caster.setSkillCast2(null);
				caster.setIsCastingSimultaneouslyNow(false);
			} else {
				caster.setSkillCast(null);
				caster.setIsCastingNow(false);
			}
			mog.exit();
			caster.notifyQuestEventSkillFinished(skill, targets[0]);
			return;
		}

		// Go through targets table
		for (WorldObject nextTarget : targets) {
			if (nextTarget.isPlayable()) {
				if (skill.getSkillType() == ESkillType.BUFF || skill.getSkillType() == ESkillType.FUSION || skill.getSkillType() == ESkillType.SEED) {
					nextTarget.getCreature().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(skill));
				}

				if (caster.isPlayer() && nextTarget.isSummon()) {
					nextTarget.getSummon().updateAndBroadcastStatus(1);
				}
			}
		}

		StatusUpdate su = new StatusUpdate(caster);
		boolean isSendStatus = false;

		// Consume MP of the Creature and Send the Server->Client packet StatusUpdate with current HP and MP to all other Player to inform
		final double mpConsume = caster.getStat().getMpConsume(skill);
		if (mpConsume > 0) {
			if (mpConsume > caster.getCurrentMp()) {
				caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_MP));
				caster.abortCast();
				return;
			}

			caster.getStatus().reduceMp(mpConsume);
			su.addAttribute(StatusUpdate.CUR_MP, (int) caster.getCurrentMp());
			isSendStatus = true;
		}

		// Consume HP if necessary and Send the Server->Client packet StatusUpdate with current HP and MP to all other Player to inform
		final double hpConsume = skill.getHpConsume();
		if (hpConsume > 0) {
			if (hpConsume > caster.getCurrentHp()) {
				caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_HP));
				caster.abortCast();
				return;
			}

			caster.getStatus().reduceHp(hpConsume, caster, true);
			su.addAttribute(StatusUpdate.CUR_HP, (int) caster.getCurrentHp());
			isSendStatus = true;
		}

		// Send StatusUpdate with MP modification to the Player
		if (isSendStatus) {
			caster.sendPacket(su);
		}

		// Launch the magic skill in order to calculate its effects
		callSkill();

		mut.phase = 3;
		if (hitTime == 0 || coolTime == 0) {
			finish(mut);
		} else {
			if (simultaneously) {
				caster.setSkillCast2(ThreadPool.schedule(mut, coolTime));
			} else {
				caster.setSkillCast(ThreadPool.schedule(mut, coolTime));
			}
		}

		if (caster.isPlayer()) {
			// check for charges

			final boolean sonicMoveConsume = SonicAssault.validate(skill.getId(), caster.getPlayer());
			if (sonicMoveConsume) {
				return;
			}

			int charges = caster.getPlayer().getCharges();
			if (skill.getMaxCharges() == 0 && charges < skill.getNumCharges()) {
				caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
				caster.abortCast();
				return;
			}

			// generate charges if any
			if (skill.getNumCharges() > 0) {
				if (skill.getMaxCharges() > 0) {
					caster.getPlayer().increaseCharges(skill.getNumCharges(), skill.getMaxCharges());
				} else {
					caster.getPlayer().decreaseCharges(skill.getNumCharges());
				}
			}
		}
	}

	public void finish(MagicUseTask mut) {
		if (simultaneously) {
			caster.setSkillCast2(null);
			caster.setIsCastingSimultaneouslyNow(false);
			return;
		}

		caster.setSkillCast(null);
		caster.setIsCastingNow(false);
		interruptTime = 0;

		final WorldObject tgt = targets.length > 0 ? targets[0] : null;

		// Attack target after skill use
		final WorldObject castersTarget = caster.getTarget();
		if (skill.nextActionIsAttack()
				&& castersTarget != null
				&& castersTarget.isCreature()
				&& castersTarget != caster
				&& castersTarget == tgt
				&& castersTarget.isAttackable()) {
			if (caster.getAI() == null || caster.getAI().getNextIntention() == null || caster.getAI().getNextIntention().getIntention() != CtrlIntention.MOVE_TO) {
				caster.getAI().setIntention(CtrlIntention.ATTACK, tgt);
			}
		}

		if (skill.isOffensive() && !(skill.getSkillType() == ESkillType.UNLOCK) && !(skill.getSkillType() == ESkillType.DELUXE_KEY_UNLOCK)) {
			caster.getAI().clientStartAutoAttack();
		}

		// Notify the AI of the Creature with EVT_FINISH_CASTING
		caster.getAI().notifyEvent(CtrlEvent.EVT_FINISH_CASTING);
		caster.notifyQuestEventSkillFinished(skill, tgt);
		caster.getEventBus().notify(new OnCast(caster, target, skill, OnCast.CastType.FINISH, targets));

		// If the current character is a summon, refresh _currentPetSkill, otherwise if it's a player, refresh _currentSkill and _queuedSkill.
		if (caster.isPlayable()) {
			boolean isPlayer = caster.isPlayer();
			final Player player = caster.getPlayer();

			if (isPlayer) {
				// Wipe current cast state.
				player.setCurrentSkill(null, false, false);

				// Check if a skill is queued.
				final SkillUseHolder queuedSkill = player.getQueuedSkill();
				if (queuedSkill.getSkill() != null) {
					ThreadPool.execute(new QueuedMagicUseTask(player, queuedSkill.getSkill(), queuedSkill.isCtrlPressed(), queuedSkill.isShiftPressed()));
					player.setQueuedSkill(null, false, false);
				}
			} else {
				player.setCurrentPetSkill(null, false, false);
			}
		}
	}

	/* FUNCTIONS *******************/
	public void onFailure() {
		if (simultaneously) {
			caster.setIsCastingSimultaneouslyNow(false);
		} else {
			caster.setIsCastingNow(false);
		}

		if (caster.isPlayer()) {
			caster.sendPacket(ActionFailed.STATIC_PACKET);
			caster.getAI().setIntention(CtrlIntention.ACTIVE);
		}
	}

	private void callSkill() {
		final boolean absorb = Formulas.calcAbsorb(caster, target, skill);
		if (skill.isProjectile()) {
			ThreadPool.schedule(() -> {
				if (absorb && caster.isPlayer()) {
					caster.getPlayer().sendPacket(SystemMessageId.ATTACK_FAILED);
					return;
				}

				caster.callSkill(skill, targets);
			}, Formulas.calcSkillFlyTime(caster, targets[0]));
		} else {
			if (absorb && caster.isPlayer()) {
				caster.getPlayer().sendPacket(SystemMessageId.ATTACK_FAILED);
				return;
			}

			caster.callSkill(skill, targets);
		}

		caster.getEventBus().notify(new OnCast(caster, target, skill, OnCast.CastType.AFTER_CALL_SKILL, targets));
		if (caster.isPlayer()) {
			if (RecoiledBlast.validate(caster.getPlayer(), skill)) {
				SkillTable.FrequentTalent.RECOILED_BLAST.getHandler().invoke(caster, target, skill);
			}
		}
	}
}
