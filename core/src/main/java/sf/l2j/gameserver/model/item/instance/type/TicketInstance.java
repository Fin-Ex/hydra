/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.item.instance.type;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sf.finex.model.GLT.EStage;
import sf.finex.model.GLT.GLTController;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.GetItem;

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
