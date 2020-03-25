/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.item.instance.type;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.model.GLT.EStage;
import net.sf.finex.model.GLT.GLTController;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.GetItem;

/**
 *
 * @author finfan
 */
@Slf4j
public class TicketInstance extends ItemInstance {
	
	@Getter @Setter private Player baseOwner;
	@Getter @Setter private int number;
	@Getter @Setter private boolean isBusy;

	public TicketInstance(int objectId, int itemId) {
		super(objectId, itemId);
		isAutodestroyable = false; // ticket must leave on the ground
	}

	@Override
	public void pickupMe(Creature pickUpper) {
		if(!pickUpper.isPlayer()) {
			return;
		}
		
		if(GLTController.getInstance().getStage() != EStage.START) {
			return;
		}
		
		if(!GLTController.getInstance().isParticipate(pickUpper.getPlayer())) {
			return;
		}
		
		pickUpper.broadcastPacket(new GetItem(this, pickUpper.getPlayer()));
		setIsVisible(false);
		log.info("ticket was pickuped");
	}
}
