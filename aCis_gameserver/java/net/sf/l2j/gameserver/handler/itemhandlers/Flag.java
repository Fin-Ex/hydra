/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.taskmanager.PvpFlagTaskManager;

/**
 *
 * @author finfan
 */
public class Flag implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (playable.getTarget() == null) {
			return;
		}

		final WorldObject target = playable.getTarget();
		if (!target.isPlayable()) {
			return;
		}

		final Player pc = target.getPlayer();
		if (pc.getPvpFlag() == 0) {
			PvpFlagTaskManager.getInstance().add(pc, Config.PVP_NORMAL_TIME);
			pc.updatePvPFlag(1);
		}
	}

}
