/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.handler.itemhandlers;

import sf.l2j.gameserver.handler.IHandler;
import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;

/**
 *
 * @author finfan
 */
public class FakeTicket implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		item.setPickupable(false);
		item.dropMe(playable, playable.getX(), playable.getY(), playable.getZ());
	}
}
