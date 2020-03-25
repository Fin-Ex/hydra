/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.zone.type;

import java.util.concurrent.locks.ReentrantLock;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.serverpackets.DeleteObject;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.skills.AbnormalEffect;

/**
 *
 * @author finfan
 */
public class L2GLTBushZone extends L2GLTZone {

	private final ReentrantLock locker = new ReentrantLock();
	private Player hider;

	public L2GLTBushZone(int id) {
		super(id);
	}

	@Override
	protected void onEnter(Creature character) {
		if (!character.isPlayer() || !character.isInsideZone(ZoneId.GLT)) {
			return;
		}

		if(hider != null) {
			character.sendMessage("Someone is near...");
			return;
		}
		
		locker.lock();
		try {
			hider = character.getPlayer();
		} finally {
			locker.unlock();
		}

		hider.getAppearance().setInvisible();

		if (hider.getAI().getNextIntention() != null && hider.getAI().getNextIntention().getIntention() == CtrlIntention.ATTACK) {
			hider.getAI().setIntention(CtrlIntention.IDLE);
		}

		final L2GameServerPacket del = new DeleteObject(hider);
		for (Creature nearCreatures : hider.getKnownType(Creature.class)) {
			if (nearCreatures.getTarget() == hider) {
				nearCreatures.removeTarget();
			}

			if (nearCreatures.isPlayer()) {
				nearCreatures.sendPacket(del);
			}
		}
		hider.startAbnormalEffect(AbnormalEffect.STEALTH);
		
		character.setInsideZone(ZoneId.GLT_BUSH, true);
		super.onEnter(character);
	}

	@Override
	protected void onExit(Creature character) {
		if (!character.isInsideZone(ZoneId.GLT)) {
			return;
		}

		locker.lock();
		try {
			if(character == hider) {
				hider.getAppearance().setVisible();
				hider.broadcastUserInfo();
				hider.stopAbnormalEffect(AbnormalEffect.STEALTH);
			}
			hider = null;
		} finally {
			locker.unlock();
		}

		character.setInsideZone(ZoneId.GLT_BUSH, false);
		super.onExit(character);
	}

	@Override
	public void onDieInside(Creature character) {
		hider = null;
		super.onDieInside(character);
	}
}
