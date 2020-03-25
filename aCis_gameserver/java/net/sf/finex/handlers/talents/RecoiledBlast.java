/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.talents;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.handler.skillhandlers.Pdam;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.EffectPoint;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.finex.model.talents.ITalentHandler;

/**
 *
 * @author finfan
 */
@Slf4j
public class RecoiledBlast implements ITalentHandler {

	private static final short SONIC_BLASTER = 6;

	@Override
	public Boolean invoke(Object... args) {
		final Player caster = (Player) args[0];
		final Creature target = (Creature) args[1];
		final L2Skill skill = (L2Skill) args[2];
		recoil(caster, target, skill, 100);
		return Boolean.TRUE;
	}

	private void recoil(Player owner, Creature lastTarget, L2Skill skill, int chance) {
		final EffectPoint blast = new EffectPoint(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(50000), owner);
		blast.detachAI();
		blast.setIsInvul(true);
		blast.spawnMe(lastTarget.getX(), lastTarget.getY(), lastTarget.getZ());
		blast.scheduleDespawn(850);

		final List<Creature> targets = blast.getKnownTypeInRadius(Creature.class, 150);
		if (targets.isEmpty()) {
			return;
		}

		Creature nextTarget = null;
		for (Creature tgt : targets) {
			if (tgt == blast || tgt == owner || tgt == lastTarget || tgt.isDead() || !tgt.isAutoAttackable(owner)) {
				continue;
			}

			nextTarget = tgt;
			break;
		}

		if (nextTarget == null) {
			return;
		}

		blast.broadcastPacket(new MagicSkillUse(blast, nextTarget, SONIC_BLASTER, 1, 750, 0));
		ThreadPool.schedule(new Task(owner, new Creature[]{nextTarget}, skill, chance), Formulas.calcSkillFlyTime(blast, nextTarget) + 375);
	}

	public static final boolean validate(Player player, L2Skill skill) {
		return skill.getId() == SONIC_BLASTER && player.hasTalent(13);
	}

	private class Task implements Runnable {

		private final Player owner;
		private final Creature[] targets;
		private final L2Skill skill;
		private int chance;

		public Task(Player owner, Creature[] targets, L2Skill skill, int chance) {
			this.owner = owner;
			this.targets = targets;
			this.skill = skill;
			this.chance = chance;
		}

		@Override
		public void run() {
			final IHandler handler = HandlerTable.getInstance().get(Pdam.class);
			if (handler != null) {
				handler.invoke(owner, skill, targets);
			} else {
				skill.useSkill(owner, targets);
			}

			chance -= 20;
			if (Rnd.calcChance(chance, 100)) {
				recoil(owner, targets[0], skill, chance);
			}
		}
	}
}
