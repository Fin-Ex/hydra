/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import java.util.List;
import lombok.Data;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

/**
 *
 * @author finfan
 */
public class Timelooop implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		item.setSpecialTask(ThreadPool.scheduleAtFixedRate(new Task(item), 15000, 20000));
	}

	@Data
	private class Task implements Runnable {

		private final ItemInstance item;
		private Player target;
		private int counter = 0;

		@Override
		public void run() {
			if (target == null) {
				final List<Player> players = item.getKnownTypeInRadius(Player.class, 80);
				if (!players.isEmpty()) {
					target = players.get(0);
				}
			}

			// with founded target we start 3 times teleport them every 20 seconds to first item point.
			if (target != null) {
				switch (counter) {
					case 0:
						target.sendMessage("You have undergone a cycle of time!");
						break;

					case 3:
						if (item.getSpecialTask() != null) {
							item.getSpecialTask().cancel(false);
							item.setSpecialTask(null);
							counter = 0;
						}

					default:
						target.teleToLocation(item.getX(), item.getY(), item.getZ(), 0);
						target.sendMessage("Time loop #" + counter);
						break;
				}
				counter++;
			}
		}

	}
}
