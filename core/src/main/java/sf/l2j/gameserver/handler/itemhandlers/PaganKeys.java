package sf.l2j.gameserver.handler.itemhandlers;


import sf.l2j.gameserver.data.DoorTable;
import sf.l2j.gameserver.handler.IHandler;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Npc;
import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.actor.instance.Door;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;

public class PaganKeys implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (!(playable instanceof Player)) {
			return;
		}

		final Player activeChar = (Player) playable;
		final WorldObject target = activeChar.getTarget();

		if (!(target instanceof Door)) {
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		final Door door = (Door) target;

		if (!(activeChar.isInsideRadius(door, Npc.INTERACTION_DISTANCE, false, false))) {
			activeChar.sendPacket(SystemMessageId.DIST_TOO_FAR_CASTING_STOPPED);
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, true)) {
			return;
		}

		final int doorId = door.getDoorId();

		switch (item.getItemId()) {
			case 8056:
				if (doorId == 23150004 || doorId == 23150003) {
					DoorTable.getInstance().getDoor(23150003).openMe();
					DoorTable.getInstance().getDoor(23150004).openMe();
				} else {
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
				}
				break;

			case 8273:
				switch (doorId) {
					case 19160002:
					case 19160003:
					case 19160004:
					case 19160005:
					case 19160006:
					case 19160007:
					case 19160008:
					case 19160009:
						DoorTable.getInstance().getDoor(doorId).openMe();
						break;

					default:
						activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
						break;
				}
				break;

			case 8275:
				switch (doorId) {
					case 19160012:
					case 19160013:
						DoorTable.getInstance().getDoor(doorId).openMe();
						break;

					default:
						activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
						break;
				}
				break;
		}
	}
}