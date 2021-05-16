package sf.l2j.gameserver.model.zone.type;

import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Npc;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.zone.L2ZoneType;
import sf.l2j.gameserver.model.zone.ZoneId;
import sf.l2j.gameserver.network.serverpackets.AbstractNpcInfo.NpcInfo;
import sf.l2j.gameserver.network.serverpackets.ServerObjectInfo;

public class L2WaterZone extends L2ZoneType {

	public L2WaterZone(int id) {
		super(id);
	}

	@Override
	protected void onEnter(Creature character) {
		character.setInsideZone(ZoneId.WATER, true);

		if (character instanceof Player) {
			((Player) character).broadcastUserInfo();
		} else if (character instanceof Npc) {
			for (Player player : character.getKnownType(Player.class)) {
				if (character.getMoveSpeed() == 0) {
					player.sendPacket(new ServerObjectInfo((Npc) character, player));
				} else {
					player.sendPacket(new NpcInfo((Npc) character, player));
				}
			}
		}
	}

	@Override
	protected void onExit(Creature character) {
		character.setInsideZone(ZoneId.WATER, false);

		if (character instanceof Player) {
			((Player) character).broadcastUserInfo();
		} else if (character instanceof Npc) {
			for (Player player : character.getKnownType(Player.class)) {
				if (character.getMoveSpeed() == 0) {
					player.sendPacket(new ServerObjectInfo((Npc) character, player));
				} else {
					player.sendPacket(new NpcInfo((Npc) character, player));
				}
			}
		}
	}

	@Override
	public void onDieInside(Creature character) {
	}

	@Override
	public void onReviveInside(Creature character) {
	}

	public int getWaterZ() {
		return getForm().getHighZ();
	}
}
