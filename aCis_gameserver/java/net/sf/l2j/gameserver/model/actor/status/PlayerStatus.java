package net.sf.l2j.gameserver.model.actor.status;

import java.util.concurrent.Future;
import lombok.Getter;
import net.sf.finex.model.regeneration.ERegenType;
import net.sf.finex.model.regeneration.RegenTask;
import net.sf.l2j.Config;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.instancemanager.DuelManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.Servitor;
import net.sf.l2j.gameserver.model.entity.Duel.DuelState;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.Stats;

public class PlayerStatus extends PlayableStatus {

	private double currentCp = 0;
	@Getter
	private boolean parryed;

	public PlayerStatus(Player activeChar) {
		super(activeChar);
	}

	@Override
	public final void reduceCp(int value) {
		if (getCurrentCp() > value) {
			setCurrentCp(getCurrentCp() - value);
		} else {
			setCurrentCp(0);
		}
	}

	@Override
	public final void reduceHp(double value, Creature attacker) {
		reduceHp(value, attacker, true, false, false, false);
	}

	@Override
	public final void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHPConsumption) {
		reduceHp(value, attacker, awake, isDOT, isHPConsumption, false);
	}

	public final void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHPConsumption, boolean ignoreCP) {
		if (getActiveChar().isDead()) {
			return;
		}

		// invul handling
		if (getActiveChar().isInvul()) {
			// other chars can't damage
			if (attacker != getActiveChar()) {
				return;
			}

			// only DOT and HP consumption allowed for damage self
			if (!isDOT && !isHPConsumption) {
				return;
			}
		}

		if (!isHPConsumption) {
			getActiveChar().stopEffectsOnDamage(awake);
			getActiveChar().forceStandUp();

			if (!isDOT) {
				if (getActiveChar().isStunned() && 12 > Rnd.get(100)) {
					getActiveChar().stopStunning(true);
				}

				if (getActiveChar().isAfraid() && 12 > Rnd.get(100)) {
					getActiveChar().stopFear(true);
				}
			}
		}

		int fullValue = (int) value;
		int tDmg = 0;

		if (attacker != null && attacker != getActiveChar()) {
			final Player attackerPlayer = attacker.getPlayer();
			if (attackerPlayer != null) {
				if (attackerPlayer.isGM() && !attackerPlayer.getAccessLevel().canGiveDamage()) {
					return;
				}

				if (getActiveChar().isInDuel()) {
					final DuelState playerState = getActiveChar().getDuelState();
					if (playerState == DuelState.DEAD || playerState == DuelState.WINNER) {
						return;
					}

					// Cancel duel if player got hit by another player that is not part of the duel or if player isn't in duel state.
					if (attackerPlayer.getDuelId() != getActiveChar().getDuelId() || playerState != DuelState.DUELLING) {
						getActiveChar().setDuelState(DuelState.INTERRUPTED);
					}
				}
			}

			// Check and calculate transfered damage
			final Summon summon = getActiveChar().getActiveSummon();
			if (summon != null && summon instanceof Servitor && MathUtil.checkIfInRange(900, getActiveChar(), summon, true)) {
				tDmg = (int) value * (int) getActiveChar().getStat().calcStat(Stats.TransferDam, 0, null, null) / 100;

				// Only transfer dmg up to current HP, it should not be killed
				tDmg = Math.min((int) summon.getCurrentHp() - 1, tDmg);
				if (tDmg > 0) {
					summon.reduceCurrentHp(tDmg, attacker, null);
					value -= tDmg;
					fullValue = (int) value; // reduce the announced value here as player will get a message about summon damage
				}
			}

			if (!ignoreCP && attacker instanceof Playable) {
				if (getCurrentCp() >= value) {
					setCurrentCp(getCurrentCp() - value); // Set Cp to diff of Cp vs value
					value = 0; // No need to subtract anything from Hp
				} else {
					value -= getCurrentCp(); // Get diff from value vs Cp; will apply diff to Hp
					setCurrentCp(0, false); // Set Cp to 0
				}
			}

			if (fullValue > 0 && !isDOT) {
				SystemMessage smsg = SystemMessage.getSystemMessage(SystemMessageId.S1_GAVE_YOU_S2_DMG);
				smsg.addCharName(attacker);
				smsg.addNumber(fullValue);
				getActiveChar().sendPacket(smsg);

				if (tDmg > 0) {
					smsg = SystemMessage.getSystemMessage(SystemMessageId.SUMMON_RECEIVED_DAMAGE_S2_BY_S1);
					smsg.addCharName(attacker);
					smsg.addNumber(tDmg);
					getActiveChar().sendPacket(smsg);

					if (attackerPlayer != null) {
						smsg = SystemMessage.getSystemMessage(SystemMessageId.GIVEN_S1_DAMAGE_TO_YOUR_TARGET_AND_S2_DAMAGE_TO_SERVITOR);
						smsg.addNumber(fullValue);
						smsg.addNumber(tDmg);
						attackerPlayer.sendPacket(smsg);
					}
				}
			}
		}

		if (value > 0) {
			value = getCurrentHp() - value;
			if (value <= 0) {
				if (getActiveChar().isInDuel()) {
					if (getActiveChar().getDuelState() == DuelState.DUELLING) {
						getActiveChar().disableAllSkills();
						stopRegen(ERegenType.VALUES);

						if (attacker != null) {
							attacker.getAI().setIntention(CtrlIntention.ACTIVE);
							attacker.sendPacket(ActionFailed.STATIC_PACKET);
						}

						// let the DuelManager know of his defeat
						DuelManager.getInstance().onPlayerDefeat(getActiveChar());
					}
					value = 1;
				} else {
					value = 0;
				}
			}
			setCurrentHp(value);
		}

		if (getActiveChar().getCurrentHp() < 0.5) {
			getActiveChar().abortAttack();
			getActiveChar().abortCast();

			if (getActiveChar().isInOlympiadMode()) {
				stopRegen(ERegenType.VALUES);
				getActiveChar().setIsDead(true);

				if (getActiveChar().getActiveSummon() != null) {
					getActiveChar().getActiveSummon().getAI().setIntention(CtrlIntention.IDLE, null);
				}

				return;
			}

			getActiveChar().doDie(attacker);

			if (!Config.DISABLE_TUTORIAL) {
				QuestState qs = getActiveChar().getQuestState("Tutorial");
				if (qs != null) {
					qs.getQuest().notifyEvent("CE30", null, getActiveChar());
				}
			}
		}
	}

	@Override
	public final void setCurrentHp(double newHp, boolean broadcastPacket) {
		super.setCurrentHp(newHp, broadcastPacket);

		if (!Config.DISABLE_TUTORIAL && getCurrentHp() <= getActiveChar().getStat().getMaxHp() * .3) {
			QuestState qs = getActiveChar().getQuestState("Tutorial");
			if (qs != null) {
				qs.getQuest().notifyEvent("CE45", null, getActiveChar());
			}
		}
	}

	@Override
	public final double getCurrentCp() {
		return currentCp;
	}

	@Override
	public final void setCurrentCp(double newCp) {
		setCurrentCp(newCp, true);
	}

	public final void setCurrentCp(double newCp, boolean broadcastPacket) {
		int maxCp = getActiveChar().getStat().getMaxCp();

		synchronized (this) {
			if (getActiveChar().isDead()) {
				return;
			}

			if (newCp < 0) {
				newCp = 0;
			}

			if (newCp >= maxCp) {
				// Set the RegenActive flag to false
				currentCp = maxCp;
				stopRegen(ERegenType.CP);
			} else {
				// Set the RegenActive flag to true
				currentCp = newCp;
				startRegen(ERegenType.CP);
			}
		}

		if (broadcastPacket) {
			getActiveChar().broadcastStatusUpdate();
		}
	}

	private Future<?> cpTask;

	@Override
	public void startRegen(ERegenType... regens) {
		super.startRegen(regens);
		for (ERegenType regenType : regens) {
			if (regenType == ERegenType.CP) {
				if (cpTask == null) {
					cpTask = ThreadPool.scheduleAtFixedRate(new RegenTask(getActiveChar(), ERegenType.CP), interval(regenType), interval(regenType));
				}
			}
		}
	}

	@Override
	public void stopRegen(ERegenType... regens) {
		super.stopRegen(regens);
		for (ERegenType regenType : regens) {
			if (regenType == ERegenType.CP) {
				if (cpTask != null) {
					cpTask.cancel(false);
					cpTask = null;
				}
			}
		}
	}

	@Override
	public int interval(ERegenType type) {
		if (type == ERegenType.CP) {
			return Math.max(getActiveChar().getStat().getCpRegenInterval(), 100);
		}

		return Math.max(super.interval(type), 100);
	}

	@Override
	public Player getActiveChar() {
		return super.getActiveChar().getPlayer();
	}
}
