/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.actor.instance;

import java.util.concurrent.locks.ReentrantLock;
import net.sf.finex.enums.ESocialAction;
import net.sf.finex.model.movie.actions.ActSocial;
import net.sf.l2j.Config;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.DropCategory;
import net.sf.l2j.gameserver.model.item.DropData;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.skills.AbnormalEffect;

/**
 *
 * @author finfan
 */
public class GLTChest extends GLTNpc {

	private final ReentrantLock locker = new ReentrantLock();

	public GLTChest(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public boolean isAutoAttackable(Creature attacker) {
		return false;
	}

	@Override
	public void onAction(Player player) {
		if (isDead()) {
			return;
		}

		locker.lock();
		try {
			if (player.getTarget() != this) {
				player.setTarget(this);
			} else {
				if (!check(player)) {
					player.getAI().setIntention(CtrlIntention.INTERACT, this);
				} else {
					player.sendPacket(new MoveToPawn(player, this, Npc.INTERACTION_DISTANCE));
					player.sendPacket(ActionFailed.STATIC_PACKET);
					new ActSocial(ESocialAction.Pickup, player).call();
					doDie(player);
				}
			}
		} finally {
			locker.unlock();
		}
	}

	private boolean check(Player player) {
		// Can't interact while casting a spell.
		if (player.isCastingNow() || player.isCastingSimultaneouslyNow()) {
			return false;
		}

		// Can't interact while died.
		if (player.isDead() || player.isFakeDeath()) {
			return false;
		}

		// Can't interact sitted.
		if (player.isSitting()) {
			return false;
		}

		// Can't interact in shop mode, or during a transaction or a request.
		if (player.isInStoreMode() || player.isProcessingTransaction()) {
			return false;
		}

		// Can't interact if regular distance doesn't match.
		return isInsideRadius(player, 100, true, false);
	}

	@Override
	public void onSpawn() {
		super.onSpawn();
		startAbnormalEffect(AbnormalEffect.MAGIC_CIRCLE);
	}

	@Override
	public boolean doDie(Creature killer) {
		if (!super.doDie(killer)) {
			return false;
		}

		final Player player = killer.getPlayer();
		for (DropCategory cat : getTemplate().getDropData()) {
			final IntIntHolder item = calculateCategorizedRewardItem(cat);
			if (item != null) {
				dropItem(player, item); // drop the item on the ground
			}
		}
		
		stopAbnormalEffect(AbnormalEffect.MAGIC_CIRCLE);
		return true;
	}

	private ItemInstance dropItem(Player player, IntIntHolder holder) {
		ItemInstance item = null;
		for (int i = 0; i < holder.getValue(); i++) {
			// Init the dropped ItemInstance and add it in the world as a visible object at the position where mob was last
			item = ItemTable.getInstance().createItem("Loot", holder.getId(), holder.getValue(), player, this);
			item.dropMe(this, getX() + Rnd.get(-70, 70), getY() + Rnd.get(-70, 70), Math.max(getZ(), player.getZ()) + 20);

			// If stackable, end loop as entire count is included in 1 instance of item
			if (item.isStackable() || !Config.MULTIPLE_ITEM_DROP) {
				break;
			}
		}
		return item;
	}

	private IntIntHolder calculateCategorizedRewardItem(DropCategory categoryDrops) {
		if (categoryDrops == null) {
			return null;
		}

		int basecategoryDropChance = categoryDrops.getCategoryChance();
		int categoryDropChance = basecategoryDropChance;

		// Set our limits for chance of drop
		if (categoryDropChance < 1) {
			categoryDropChance = 1;
		}

		// Check if an Item from this category must be dropped
		if (Rnd.get(DropData.MAX_CHANCE) < categoryDropChance) {
			DropData drop = categoryDrops.dropOne(isRaid() && !isRaidMinion());
			if (drop == null) {
				return null;
			}

			double dropChance = drop.getChance();

			// Get min and max Item quantity that can be dropped in one time
			final int min = drop.getMinDrop();
			final int max = drop.getMaxDrop();

			// Get the item quantity dropped
			int itemCount = 0;

			// Check if the Item must be dropped
			int random = Rnd.get(DropData.MAX_CHANCE);
			while (random < dropChance) {
				// Get the item quantity dropped
				if (min < max) {
					itemCount += Rnd.get(min, max);
				} else if (min == max) {
					itemCount += min;
				} else {
					itemCount++;
				}

				// Prepare for next iteration if dropChance > L2DropData.MAX_CHANCE
				dropChance -= DropData.MAX_CHANCE;
			}

			if (itemCount > 0) {
				return new IntIntHolder(drop.getItemId(), itemCount);
			}
		}
		return null;
	}
}
