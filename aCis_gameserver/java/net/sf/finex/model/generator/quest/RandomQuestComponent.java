/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest;

import lombok.Getter;
import lombok.Setter;
import net.sf.finex.AbstractComponent;
import net.sf.finex.data.RandomQuestData;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
public class RandomQuestComponent extends AbstractComponent {

	@Getter @Setter private RandomQuestData quest;
	@Setter private long timeStamp;

	public RandomQuestComponent(Player player) {
		super(player);
	}
	
	public boolean hasQuest() {
		return quest != null;
	}
	
	public boolean hasPenalty() {
		return timeStamp != 0 && timeStamp > System.currentTimeMillis();
	}
	
	/**
	 * TODO: Shareing quests between players in party
	 * checks questPass by party members if they have a quest
	 * Share the current accepted quest with others in party.
	 */
	public void share() {
		if(!getGameObject().isInParty()) {
			getGameObject().sendMessage("Can't share without party.");
			return;
		}
		
		for(Player player : getGameObject().getParty().getMembers()) {
			final RandomQuestComponent comp = player.getComponent(RandomQuestComponent.class);
			if(comp.hasQuest()) {
				getGameObject().sendMessage("Player " + player.getName() + " already has a quest.");
				continue;
			}
			
			if(player.isDead()) {
				continue;
			}
			
			//player.sendPacket(new ConfirmDlg(SystemMessage.getSystemMessage(SystemMessageId.S1_SHARE_S2_QUEST_ACCEPT)).addCharName(getGameObject()).addString(quest.getName()));
		}
	}
	
	@Override
	public Player getGameObject() {
		return super.getGameObject().getPlayer();
	}
	
	@Override
	public void onAdd() {
		restore();
	}

	@Override
	public void onRemove() {
		// will never be removed
	}

	@Override
	public void store() {
	}

	@Override
	public void restore() {
	}

	@Override
	public void delete() {
	}

	@Override
	public void remove(Object... args) {
	}
}
