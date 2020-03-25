package net.sf.l2j.gameserver.model.zone.type;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.serverpackets.AbstractNpcInfo.NpcInfo;
import net.sf.l2j.gameserver.network.serverpackets.ServerObjectInfo;

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
