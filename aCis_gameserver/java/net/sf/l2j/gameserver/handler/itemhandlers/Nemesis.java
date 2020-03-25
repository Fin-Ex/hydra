/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.ai.type.NemesisAI;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;

/**
 *
 * @author finfan
 */
public class Nemesis implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		final WorldObject obj = playable.getTarget();
		if (obj == null || !obj.isPlayer()) {
			spawnCreatureAndSetTheTarget(playable, null);
			return;
		}

		final Player target = obj.getPlayer();
		if (target.isDead() || target.getClassId().equalsOrChildOf(ClassId.Necromancer)) {
			spawnCreatureAndSetTheTarget(playable, null);
			return;
		}

		spawnCreatureAndSetTheTarget(playable, target);
	}

	private void spawnCreatureAndSetTheTarget(Playable caster, Player target) {
		final NpcTemplate template = NpcTable.getInstance().getTemplate(50001);
		final Monster nemesis = new Monster(IdFactory.getInstance().getNextId(), template);

		nemesis.setTarget(target != null ? target : caster);
		nemesis.setIsInvul(true);
		nemesis.setIsRunning(false);
		nemesis.setFullHpMpCp();
		nemesis.spawnMe(caster.getX(), caster.getY(), caster.getZ());

		// start AI attack and folow for target
		nemesis.getAI().setIntention(CtrlIntention.ATTACK, target != null ? target : caster);
		nemesis.setIsNoRndWalk(true);
		nemesis.broadcastNpcSay(target != null ? target.getName() : caster.getName() + NemesisAI.getQuote());
	}
}
