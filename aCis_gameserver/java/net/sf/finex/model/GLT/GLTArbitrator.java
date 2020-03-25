/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.item.instance.type.HunterCardInstance;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.instance.type.TicketInstance;

/**
 *
 * @author finfan
 */
@Slf4j
public class GLTArbitrator {
	
	public static final int HUNTER_CARD_ID = 9216;
	public static final int HUNTER_TICKET = 10000;
	
	public static final boolean checkRegistrating(Player player) {
		/**
		 * Check free places for registering
		 */
		if(GLTController.getInstance().getParticipants().size() == GLTSettings.MAX_PARTICIPANTS) {
			log.info("Already has maximum participants.");
			return false;
		}
		
		/**
		 * Level check
		 */
		if(player.getLevel() < GLTSettings.MIN_LEVEL && player.getLevel() > GLTSettings.MAX_LEVEL) {
			log.info("Level of {} is too hight or too low not in range of {}-{}.", player.getName(), GLTSettings.MIN_LEVEL, GLTSettings.MAX_LEVEL);
			return false;
		}
		
		/**
		 * Classification check
		 */
		final int restrictedLevel = GLTSettings.RESTRICTED_CLASS_LEVEL;
		final ClassId playerClass = player.getClassId();
		if (GLTSettings.RESTRICTED_CLASSES != null) {
			for(ClassId next : GLTSettings.RESTRICTED_CLASSES) {
				if(next.equalsOrChildOf(playerClass)) {
					if(restrictedLevel > -1 && playerClass.level() <= restrictedLevel) {
						log.info("Class {} cannot participate cause his class level is {} but need {}, change settings in GLT Settings.java", playerClass, playerClass.level(), restrictedLevel);
						return false;
					}
					log.info("Class and his childs {} cannot participate, change settings in GLT Settings.java", next);
					return false;
				}
			}
		} else if(restrictedLevel > -1) {
			if(playerClass.level() <= restrictedLevel) {
				log.info("Class {} cant participate cause his class level is {} but need {}, change settings in GLT Settings.java", playerClass, playerClass.level(), restrictedLevel);
				return false;
			}
		}
		
		/**
		 * Item enchant check
		 */
		final int armorEnch = GLTSettings.LIMIT_ARMOR_ENCHANT;
		final int wpnEnch = GLTSettings.LIMIT_WEAPON_ENCHANT;
		final int jewelEnch = GLTSettings.LIMIT_JEWEL_ENCHANT;
		for(ItemInstance invItem : player.getInventory().getItems()) {
			if(invItem.isEquipped()) {
				if(invItem.isArmor() && armorEnch > -1) {
					if(invItem.getEnchantLevel() > armorEnch) {
						log.info("Cannot participate with equipped item which enchanted more than {}", invItem.getName(), armorEnch);
						return false;
					}
				}
				if(invItem.isWeapon() && wpnEnch > -1) {
					if(invItem.getEnchantLevel() > wpnEnch) {
						log.info("Cannot participate with equipped item which enchanted more than {}", invItem.getName(), wpnEnch);
						return false;
					}
				}
				if(invItem.isWeapon() && jewelEnch > -1) {
					if(invItem.getEnchantLevel() > jewelEnch) {
						log.info("Cannot participate with equipped item which enchanted more than {}", invItem.getName(), jewelEnch);
						return false;
					}
				}
			}
		}
		
		/**
		 * Hunter card checking
		 */
		final HunterCardInstance item = player.getInventory().getHunterCardInstance();
		if(item != null && item.isValid()) {
			log.info("{} already have a valid card!", player);
			return false;
		}
		
		/**
		 * Adena contribution check
		 */
		if(!player.reduceAdena("GLT_checkRegistrating", GLTSettings.ADENA_CONTRIBUTION, null, true)) {
			log.info("Not enought adena for registrating.");
			return false;
		}
		
		log.info("Succes registration for {}", player);
		return true;
	}
	
	public static final boolean checkStage(EStage stage) {
		return GLTController.getInstance().getStage() == stage;
	}
	
	/**
	 * Create tickets for event and shuffle it.
	 * @return 
	 */
	public static final boolean createTickets() {
		final List<GLTParticipant> participants = GLTController.getInstance().getParticipants();
		final int registeredCount = participants.size();
		if(registeredCount < GLTSettings.MIN_PARTICIPANTS) {
			log.info("Not enought participants.");
			return false;
		}
	
		final List<TicketInstance> tempItems = new ArrayList<>();
		try {
			for (int i = 0; i < registeredCount; i++) {
				final TicketInstance newTicket = ItemTable.getInstance().createItem(TicketInstance.class, "GLT_ticketsCreate", HUNTER_TICKET, 1, null, null);
				newTicket.setNumber(i + 1);
				tempItems.add(newTicket);
			}
			
			Collections.shuffle(tempItems);
			GLTController.getInstance().getTickets().addAll(tempItems);
		} catch (Exception e) {
			log.error("Something goes wrong with item temp tickets creation.", e);
			return false;
		}
		
		return true;
	}
}
