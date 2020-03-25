/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.actor.instance;

import java.util.List;
import net.sf.finex.model.GLT.GLTArbitrator;
import net.sf.finex.model.GLT.GLTController;
import net.sf.finex.model.generator.quest.RandomQuestComponent;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.instancemanager.SevenSigns;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.events.OnTalk;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.scripting.EventType;
import net.sf.l2j.gameserver.scripting.Quest;

/**
 *
 * @author finfan
 */
public class HunterGuildPresident extends GLTNpc {

	public HunterGuildPresident(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command) {
		if (command.equalsIgnoreCase("GLT_registration")) {
			if(GLTArbitrator.checkRegistrating(player)) {
				GLTController.getInstance().register(player);
			}
		} else {
			super.onBypassFeedback(player, command);
		}
	}

	@Override
	public void showChatWindow(Player player, int val) {
		final int npcId = getNpcId();
		String filename;

		if (npcId >= 31865 && npcId <= 31918) {
			filename = SevenSigns.SEVEN_SIGNS_HTML_PATH + "rift/GuardianOfBorder.htm";
		} else {
			filename = getHtmlPath(npcId, val);
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		html.replace("%objectName%", getName());
		html.replace("%playerName%", player.getName());
		html.replace("%playerClass%", player.getClassId().name());
		player.sendPacket(html);

		// Send a Server->Client ActionFailed to the Player in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	@Override
	public void onAction(Player player) {
		// Set the target of the player
		if (player.getTarget() != this) {
			player.setTarget(this);
		} else {
			// Check if the player is attackable (without a forced attack) and isn't dead
			if (isAutoAttackable(player)) {
				player.getAI().setIntention(CtrlIntention.ATTACK, this);
			} else {
				// Calculate the distance between the Player and the L2Npc
				if (!canInteract(player)) {
					// Notify the Player AI with INTERACT
					player.getAI().setIntention(CtrlIntention.INTERACT, this);
				} else {
					// Rotate the player to face the instance
					player.sendPacket(new MoveToPawn(player, this, Npc.INTERACTION_DISTANCE));

					// Send ActionFailed to the player in order to avoid he stucks
					player.sendPacket(ActionFailed.STATIC_PACKET);

					if (hasRandomAnimation()) {
						onRandomAnimation(Rnd.get(8));
					}

					List<Quest> qlsa = getTemplate().getEventQuests(EventType.QUEST_START);
					if (qlsa != null && !qlsa.isEmpty()) {
						player.setLastQuestNpcObject(getObjectId());
					}

					List<Quest> qlst = getTemplate().getEventQuests(EventType.ON_FIRST_TALK);
					if (qlst != null && qlst.size() == 1) {
						qlst.get(0).notifyFirstTalk(this, player);
					} else {
						showChatWindow(player);
					}

					getEventBus().notify(new OnTalk(player, this));

					final RandomQuestComponent compoennt = player.getComponent(RandomQuestComponent.class);
					if (compoennt.hasQuest()) {
						compoennt.getQuest().getType().getHandler().getEventBus().notify(new OnTalk(player, this));
					}
				}
			}
		}
	}
}
