/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.instance.EffectPoint;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;

/**
 *
 * @author finfan
 */
@Slf4j
@Deprecated
public class C4 implements IHandler {

	private final PlaySound sound = new PlaySound("GltSound.watch_out_now_explode");

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		item.setPickupable(false);
		item.setAutodestroyable(false);
		item.setIsVisible(false);
		item.dropMe(playable, playable.getX(), playable.getY(), playable.getZ());
		item.setSpecialTask(ThreadPool.scheduleAtFixedRate(new Task(item), 15000, 1000));
	}

	@Data
	private class Task implements Runnable {

		private final ItemInstance item;
		private int counter = 0;

		@Override
		public void run() {
			log.info("C4 targets check in radius 200...");
			List<Creature> creatures = item.getKnownTypeInRadius(Creature.class, 80);
			if (!creatures.isEmpty()) {
				creatures = item.getKnownTypeInRadius(Creature.class, 200);
				final EffectPoint c4 = new EffectPoint(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(50000), null);
				c4.detachAI();
				c4.setIsInvul(true);
				c4.spawnMe(item.getX(), item.getY(), item.getZ());
				c4.scheduleDespawn(3500);

				if (counter == 2) {
					c4.broadcastPacket(new MagicSkillUse(c4, 4143, 1, 900, 0));
				}

				creatures.forEach(next -> {
					switch (counter) {
						case 0:
							next.sendPacket(sound);
							break;

						case 3:
							next.reduceCurrentHp(next.getMaxHp(), null, null);
							break;
					}
				});

				if (counter == 3 && item.getSpecialTask() != null) {
					item.getSpecialTask().cancel(false);
					item.setSpecialTask(null);
					ItemTable.getInstance().destroyItem("C4Explosion", item, null, null);
				}

				counter++;
				log.info("C4 {} targets found, start detonate!", creatures.size());
			}
		}
	}
}
