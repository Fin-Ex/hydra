package net.sf.l2j.gameserver.handler.skillhandlers;


import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.model.item.type.WeaponType;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.model.zone.type.L2FishingZone;
import net.sf.l2j.gameserver.model.zone.type.L2WaterZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class Fishing implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.FISHING
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (!(activeChar instanceof Player)) {
			return;
		}

		Player player = (Player) activeChar;

		if (player.isFishing()) {
			if (player.getFishCombat() != null) {
				player.getFishCombat().doDie(false);
			} else {
				player.endFishing(false);
			}
			// Cancels fishing
			player.sendPacket(SystemMessageId.FISHING_ATTEMPT_CANCELLED);
			return;
		}

		// Fishing poles arent installed
		Weapon weaponItem = player.getActiveWeaponItem();
		if ((weaponItem == null || weaponItem.getItemType() != WeaponType.FISHINGROD)) {
			player.sendPacket(SystemMessageId.FISHING_POLE_NOT_EQUIPPED);
			return;
		}

		// Baits arent equipped
		ItemInstance lure = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if (lure == null) {
			player.sendPacket(SystemMessageId.BAIT_ON_HOOK_BEFORE_FISHING);
			return;
		}

		player.setLure(lure);
		ItemInstance lure2 = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);

		// Not enough baits
		if (lure2 == null || lure2.getCount() < 1) {
			player.sendPacket(SystemMessageId.NOT_ENOUGH_BAIT);
			return;
		}

		// You can't fish while you are on boat
		if (player.isInBoat()) {
			player.sendPacket(SystemMessageId.CANNOT_FISH_ON_BOAT);
			return;
		}

		if (player.isCrafting() || player.isInStoreMode()) {
			player.sendPacket(SystemMessageId.CANNOT_FISH_WHILE_USING_RECIPE_BOOK);
			return;
		}

		// You can't fish in water
		if (player.isInsideZone(ZoneId.WATER)) {
			player.sendPacket(SystemMessageId.CANNOT_FISH_UNDER_WATER);
			return;
		}

		/*
		 * If fishing is enabled, decide where will the hook be cast...
		 */
		int rnd = Rnd.get(150) + 50;
		double angle = MathUtil.convertHeadingToDegree(player.getHeading());
		double radian = Math.toRadians(angle);
		double sin = Math.sin(radian);
		double cos = Math.cos(radian);
		int x = player.getX() + (int) (cos * rnd);
		int y = player.getY() + (int) (sin * rnd);
		int z = player.getZ() + 50;
		/*
		 * ...and if the spot is in a fishing zone. If it is, it will position the hook on the water surface. If not, you have to be GM to proceed past here... in that case, the hook will be positioned using the old Z lookup method.
		 */
		L2FishingZone aimingTo = null;
		L2WaterZone water = null;
		boolean canFish = false;
		for (L2ZoneType zone : ZoneManager.getInstance().getZones(x, y)) {
			if (zone instanceof L2FishingZone) {
				aimingTo = (L2FishingZone) zone;
				continue;
			}

			if (zone instanceof L2WaterZone) {
				water = (L2WaterZone) zone;
			}
		}

		if (aimingTo != null) {
			// geodata enabled, checking if we can see end of the pole
			if (GeoEngine.getInstance().canSeeTarget(player, new Location(x, y, z))) {
				// finding z level for hook
				if (water != null) {
					// water zone exist
					if (GeoEngine.getInstance().getHeight(x, y, z) < water.getWaterZ()) {
						// water Z is higher than geo Z
						z = water.getWaterZ() + 10;
						canFish = true;
					}
				} else {
					// no water zone, using fishing zone
					if (GeoEngine.getInstance().getHeight(x, y, z) < aimingTo.getWaterZ()) {
						// fishing Z is higher than geo Z
						z = aimingTo.getWaterZ() + 10;
						canFish = true;
					}
				}
			}
		}

		if (!canFish) {
			// You can't fish here
			player.sendPacket(SystemMessageId.CANNOT_FISH_HERE);
			return;
		}

		// Has enough bait, consume 1 and update inventory. Start fishing follows.
		lure2 = player.getInventory().destroyItem("Consume", player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND), 1, player, null);
		InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(lure2);
		player.sendPacket(iu);

		// If everything else checks out, actually cast the hook and start fishing... :P
		player.startFishing(new Location(x, y, z));
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
