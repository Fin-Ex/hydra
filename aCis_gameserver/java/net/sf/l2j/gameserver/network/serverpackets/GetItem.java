package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;

/**
 * format ddddd
 */
public class GetItem extends L2GameServerPacket {

	private final ItemInstance item;
	private final Playable playable;

	public GetItem(ItemInstance item, Playable playable) {
		this.item = item;
		this.playable = playable;
	}

	public GetItem(Playable playable) {
		this.item = null;
		this.playable = playable;
	}

	@Override
	protected final void writeImpl() {
		writeC(0x0d);
		writeD(playable.getObjectId());
		if (item != null) {
			writeD(item.getObjectId());
			writeD(item.getX());
			writeD(item.getY());
			writeD(item.getZ());
		} else {
			// fake pick up just for animation
			writeD(0);
			writeD(playable.getX());
			writeD(playable.getY());
			writeD(playable.getZ());
		}
	}
}
