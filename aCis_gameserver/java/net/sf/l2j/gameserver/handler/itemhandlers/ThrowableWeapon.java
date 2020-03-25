/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.finex.model.GLT.GLTController;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author finfan
 */
public class ThrowableWeapon implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];

		if(!playable.isPlayer()) {
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if(playable.getTarget() == null || playable.getTarget() == playable) {
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final boolean isGLT = item.getEtcItem().getItemType() == EtcItemType.GLT_ITEM;
		final Creature target = playable.getTarget().getCreature();
		if(isGLT && (!GLTController.getInstance().isParticipate(playable.getPlayer()) || target.isInsideZone(ZoneId.GLT))) {
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final Player thrower = playable.getPlayer();
		final String type = item.getName();
		L2Skill castedSkill = null;
		switch(type) {
			case "Knife":
				castedSkill = SkillTable.getInstance().getInfo(7041, 1);
				break;
				
			case "Spear":
				castedSkill = SkillTable.getInstance().getInfo(7042, 1);
				break;
				
			case "Axe":
				castedSkill = SkillTable.getInstance().getInfo(7043, 1);
				break;
				
			case "Shuriken":
				castedSkill = SkillTable.getInstance().getInfo(7044, 1);
				break;
				
			default:
				throw new UnsupportedOperationException(type + " not handled in " + getClass().getSimpleName());
		}
		
		try {
			thrower.abortAttack();
			thrower.stopMove(null);
			thrower.abortCast();
			thrower.doCast(castedSkill);
		} catch (NullPointerException e) {
			throw new UnsupportedOperationException("Skill with prefix `" + type + "` is not handled (not exist in .xml)");
		}
	}
	
}
