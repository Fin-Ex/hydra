/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import lombok.Getter;
import lombok.Setter;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.location.Location;

/**
 *
 * @author finfan
 */
@Getter
public class GLTParticipant {

	private final Player player;
	private final String name;
	private final int objectId;
	
	@Setter private int ownNumber;
	@Setter private int huntNumber;
	@Setter private Location teleportLocation;
	@Setter private int grave;
	
	public volatile int safetyTime = GLTSettings.ZONE_LEAVE_SAFETY_TIME;
	
	public GLTParticipant(Player player) {
		this.player = player;
		this.name = player.getName();
		this.objectId = player.getObjectId();
	}
	
	public void addTicket(ItemInstance ticket, WorldObject reference) {
		if(player != null && player.isOnline()) {
			player.addItem("AddTicket", ticket, reference, true);
		}
	}
}
