package sf.l2j.gameserver.handler.itemhandlers;

import sf.l2j.gameserver.handler.IHandler;

import sf.l2j.gameserver.model.ShotType;
import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import sf.l2j.gameserver.model.item.kind.Weapon;
import sf.l2j.gameserver.model.item.type.WeaponType;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import sf.l2j.gameserver.util.Broadcast;

/**
 * @author -Nemesiss-
 */
public class FishShots implements IHandler {

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

		if (weaponInst == null || weaponItem.getItemType() != WeaponType.FISHINGROD) {
			return;
		}

		// Fishshot is already active
		if (activeChar.isChargedShot(ShotType.FISH_SOULSHOT)) {
			return;
		}

		// Wrong grade of soulshot for that fishing pole.
		if (weaponItem.getCrystalType() != item.getItem().getCrystalType()) {
			activeChar.sendPacket(SystemMessageId.WRONG_FISHINGSHOT_GRADE);
			return;
		}

		if (!activeChar.destroyItemWithoutTrace("Consume", item.getObjectId(), 1, null, false)) {
			activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS);
			return;
		}

		activeChar.setChargedShot(ShotType.FISH_SOULSHOT, true);
		Broadcast.toSelfAndKnownPlayers(activeChar, new MagicSkillUse(activeChar, item.getItem().getStaticSkills().get(0).getId(), 1, 0, 0));
	}
}
