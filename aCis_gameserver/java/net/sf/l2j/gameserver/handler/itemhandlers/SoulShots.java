package net.sf.l2j.gameserver.handler.itemhandlers;


import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.util.Broadcast;

public class SoulShots implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (!(playable instanceof Player)) {
			return;
		}

		final Player activeChar = (Player) playable;
		final ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		final Weapon weaponItem = activeChar.getActiveWeaponItem();
		final int itemId = item.getItemId();

		// Check if soulshot can be used
		if (weaponInst == null || weaponItem.getSoulShotCount() == 0) {
			if (!activeChar.getAutoSoulShot().contains(itemId)) {
				activeChar.sendPacket(SystemMessageId.CANNOT_USE_SOULSHOTS);
			}
			return;
		}

		if (weaponItem.getCrystalType() != item.getItem().getCrystalType()) {
			if (!activeChar.getAutoSoulShot().contains(itemId)) {
				activeChar.sendPacket(SystemMessageId.SOULSHOTS_GRADE_MISMATCH);
			}

			return;
		}

		// Check if Soulshot are already active.
		if (activeChar.isChargedShot(ShotType.SOULSHOT)) {
			return;
		}

		// Consume Soulshots if player has enough of them.
		int ssCount = weaponItem.getSoulShotCount();
		if (weaponItem.getReducedSoulShot() > 0 && Rnd.get(100) < weaponItem.getReducedSoulShotChance()) {
			ssCount = weaponItem.getReducedSoulShot();
		}

		if (!activeChar.destroyItemWithoutTrace("Consume", item.getObjectId(), ssCount, null, false)) {
			if (!activeChar.disableAutoShot(itemId)) {
				activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS);
			}

			return;
		}

		weaponInst.setChargedShot(ShotType.SOULSHOT, true);
		activeChar.sendPacket(SystemMessageId.ENABLED_SOULSHOT);
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, new MagicSkillUse(activeChar, activeChar, item.getItem().getStaticSkills().get(0).getId(), 1, 0, 0), 600);
	}
}
