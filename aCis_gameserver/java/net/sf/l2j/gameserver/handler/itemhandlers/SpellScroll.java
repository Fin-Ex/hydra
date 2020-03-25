/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.SpellItem;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 *
 * @author finfan
 */
@Deprecated
public class SpellScroll implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable user = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
	
		if (user.isCastingNow()) {
			user.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addItemName(item));
			return;
		}
		
		if (user.getTarget() == null) {
			user.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
			return;
		}
		
		if(item.isEtc() && (item.getItem() instanceof SpellItem)) {
			final SpellItem spellItem = (SpellItem) item.getItem();
			try {
				for (IntIntHolder next : spellItem.getSpells()) {
					synchronized (user) {
						user.doCast(next.getSkill());
						user.wait();
					}
				}
			} catch (InterruptedException ex) {
				log.error("", ex);
			}
		} else {
			log.error("");
		}
	}
}
