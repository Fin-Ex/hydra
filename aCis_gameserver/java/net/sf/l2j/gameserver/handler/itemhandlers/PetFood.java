package net.sf.l2j.gameserver.handler.itemhandlers;


import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Pet;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * @author Kerberos
 */
public class PetFood implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		int itemId = item.getItemId();
		switch (itemId) {
			case 2515: // Wolf's food
				useFood(playable, 2048, item);
				break;
			case 4038: // Hatchling's food
				useFood(playable, 2063, item);
				break;
			case 5168: // Strider's food
				useFood(playable, 2101, item);
				break;
			case 5169: // ClanHall / Castle Strider's food
				useFood(playable, 2102, item);
				break;
			case 6316: // Wyvern's food
				useFood(playable, 2180, item);
				break;
			case 7582: // Baby Pet's food
				useFood(playable, 2048, item);
				break;
		}
	}

	public boolean useFood(Playable activeChar, int magicId, ItemInstance item) {
		L2Skill skill = SkillTable.getInstance().getInfo(magicId, 1);
		if (skill != null) {
			if (activeChar instanceof Pet) {
				Pet pet = (Pet) activeChar;
				if (pet.destroyItem("Consume", item.getObjectId(), 1, null, false)) {
					// Send visual effect.
					activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, magicId, 1, 0, 0));

					// Put current value.
					pet.setCurrentFed(pet.getCurrentFed() + (skill.getFeed() * Config.PET_FOOD_RATE));

					// If pet is still hungry, send an alert.
					if (pet.checkAutoFeedState()) {
						pet.getPlayer().sendPacket(SystemMessageId.YOUR_PET_ATE_A_LITTLE_BUT_IS_STILL_HUNGRY);
					}

					return true;
				}
			} else if (activeChar instanceof Player) {
				final Player player = ((Player) activeChar);
				final int itemId = item.getItemId();

				if (player.isMounted() && player.getPetTemplate().canEatFood(itemId)) {
					if (player.destroyItem("Consume", item.getObjectId(), 1, null, false)) {
						player.broadcastPacket(new MagicSkillUse(activeChar, activeChar, magicId, 1, 0, 0));
						player.setCurrentFeed(player.getCurrentFeed() + (skill.getFeed() * Config.PET_FOOD_RATE));
					}
					return true;
				}

				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addItemName(itemId));
				return false;
			}
		}
		return false;
	}
}
