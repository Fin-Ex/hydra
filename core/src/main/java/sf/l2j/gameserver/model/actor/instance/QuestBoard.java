/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.actor.instance;

import java.util.StringTokenizer;
import sf.finex.data.RandomQuestData;
import sf.finex.enums.EGradeType;
import sf.finex.enums.ETownType;
import sf.finex.model.generator.quest.RandomQuestComponent;
import sf.finex.model.generator.quest.RandomQuestManager;
import sf.finex.model.generator.quest.RandomQuestHtmlManager;
import sf.l2j.gameserver.data.MapRegionTable;
import sf.l2j.gameserver.model.actor.Npc;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 *
 * @author FinFan
 */
public class QuestBoard extends Npc {

	public QuestBoard(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command) {
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		if (command.startsWith("rndquest")) {
			final int questId = Integer.parseInt(st.nextToken());
			RandomQuestHtmlManager.getInstance().showQuestDescription(player, this, questId);
		} else if (command.startsWith("takeQuest")) {
			final ETownType town = ETownType.getTownById(MapRegionTable.getTown(getX(), getY(), getZ()).getTownId());
			final EGradeType grade = EGradeType.getPlayerGrade(player);
			final int questId = Integer.parseInt(st.nextToken());
			final RandomQuestData quest = RandomQuestManager.getInstance().getQuest(town, grade, questId);
			RandomQuestManager.getInstance().addQuest(quest, player, this);
		} else if (command.startsWith("cancelQuest")) {
			RandomQuestManager.getInstance().cancelQuest(player);
		} else if (command.startsWith("showQuestList")) {
			showChatWindow(player);
		} else {
			super.onBypassFeedback(player, command);
		}
	}

	@Override
	public void showChatWindow(Player player) {
		final RandomQuestComponent component = player.getComponent(RandomQuestComponent.class);
		if (component.hasQuest()) {
			final RandomQuestData quest = component.getQuest();
			if (quest.getBoardId() == getNpcId()) {
				// show quest description
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				final StringBuilder sb = new StringBuilder();
				sb.append("<html><title>").append(quest.getName()).append("</title><body>");
				sb.append(quest.getDescription());
				sb.append("</body></html>");
				html.setHtml(sb.toString());
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
			} else {
				// show message "You already have the quest"
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setHtml("<html><title>Quest Board</title><body><br>You already have the quest. Cancel it or complete.</body></html>");
				player.sendPacket(html);
			}
			player.sendPacket(ActionFailed.STATIC_PACKET);
		} else {
			RandomQuestHtmlManager.getInstance().showQuestList(player, this);
		}
	}
}
