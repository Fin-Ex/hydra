/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.data.RandomQuestData;
import net.sf.finex.enums.EGradeType;
import net.sf.finex.enums.ETownType;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 *
 * @author FinFan
 */
@Slf4j
public class RandomQuestHtmlManager {

	@Getter
	private static final RandomQuestHtmlManager instance = new RandomQuestHtmlManager();
	@Getter
	private final Map<ETownType, Map<EGradeType, StringBuilder>> questListTable = new HashMap<>();
	@Getter
	private final Map<Integer, String> questDescription = new HashMap<>();

	public void build() {
		buildQuestList();
		buildQuestDescription();
	}

	public void buildQuestList() {
		questListTable.clear();
		for (ETownType town : ETownType.VALUES) {
			questListTable.put(town, new HashMap<>());
			final Map<EGradeType, StringBuilder> temp = questListTable.get(town);
			for (EGradeType grade : town.getGrades()) {
				final StringBuilder sb = new StringBuilder();
				sb.append("<html><title>Quest Board [").append(grade).append(" - Grade]</title><body>");
				final List<RandomQuestData> randomQuestList = RandomQuestManager.getInstance().getHolder().get(town).get(grade);
				sb.append("<table>");
				for (RandomQuestData questData : randomQuestList) {
					sb.append("<tr><td>");
					sb.append("<a action=\"bypass -h npc_%objectId%_rndquest ")
							.append(questData.getId()).append("\">")
							.append(questData.getName()).append(" [")
							.append(questData.getType()).append("]</a>");
					sb.append("</td></tr>");
				}
				sb.append("</table>");
				sb.append("</body></html>");
				temp.put(grade, sb);
			}
		}
	}

	public void buildQuestDescription() {
		questDescription.clear();
		for (ETownType town : ETownType.VALUES) {
			for (EGradeType grade : town.getGrades()) {
				final List<RandomQuestData> randomQuestList = RandomQuestManager.getInstance().getHolder().get(town).get(grade);
				for (RandomQuestData questData : randomQuestList) {
					final StringBuilder descrBuilder = new StringBuilder();
					descrBuilder.append("<html><title>").append(questData.getName()).append(" (").append(grade.getMinLevel()).append("~").append(grade.getMaxLevel())
							.append("</title><body><br>");
					descrBuilder.append(questData.getDescription());

					// build rewards
					descrBuilder.append("<br><center><font color=LEVEL>Rewards</font>");
					descrBuilder.append("<table width=100><tr>");
					if (questData.getExp() > 0) {
						descrBuilder.append("<td><img src=\"v1c01.etc_exp_point_i00\" width=32 height=32></td>");
					}
					if (questData.getSp() > 0) {
						descrBuilder.append("<td><img src=\"v1c01.etc_sp_point_i00\" width=32 height=32></td>");
					}
					if (questData.getRewards() != null) {
						descrBuilder.append("<td><img src=\"v1c01.etc_quest_add_reward_i00\" width=32 height=32></td>");
					}
					descrBuilder.append("</tr></table></center>");

					descrBuilder.append("</body></html>");
					questDescription.put(questData.getId(), descrBuilder.toString());
				}
			}
		}
	}

	private StringBuilder buildRewardDescr(StringBuilder sb, RandomQuestData quest) {
		sb.append("<table><tr>");
		if (quest.getExp() > 0) {
			sb.append("<td><img src=\"v1c01.etc_exp_point_i00\" width=32 height=32></td>");
		}
		if (quest.getSp() > 0) {
			sb.append("<td><img src=\"v1c01.etc_sp_point_i00\" width=32 height=32></td>");
		}
		if (quest.getRewards() != null) {
			sb.append("<td><img src=\"v1c01.etc_quest_add_reward_i00\" width=32 height=32></td>");
		}
		sb.append("</tr></table>");
		return sb;
	}

	/**
	 * Show quests in list in one HTML window.
	 *
	 * @param player
	 * @param npc
	 */
	public void showQuestList(Player player, Npc npc) {
		final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		if (RandomQuestManager.getInstance().isLocked()) {
			html.setHtml("<html><title>Quest Board</title><body>Getting information... Wait a bit.</body></html>");
		} else {
			final StringBuilder msg = questListTable.get(npc.getTown()).get(EGradeType.getPlayerGrade(player));
			if (msg != null) {
				html.setHtml(msg.toString());
				html.replace("%objectId%", npc.getObjectId());
			} else {
				html.setHtml("<html><title>Quest Board</title><body><br>There are no quests for your grade-quality.</body></html>");
			}
		}

		player.sendPacket(html);
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	/**
	 * Show Quest description of a current given quest by their <b>id</b>
	 *
	 * @param player
	 * @param npc
	 * @param questId
	 */
	public void showQuestDescription(Player player, Npc npc, int questId) {
		final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		if (RandomQuestManager.getInstance().isLocked()) {
			html.setHtml("<html><title>Quest Board</title><body>Getting information... Wait a bit.</body></html>");
		} else {
			html.setHtml(questDescription.get(questId));
			html.replace("%objectId%", npc.getObjectId());
		}
		player.sendPacket(html);
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	@Data
	public static class QuestHtmlData {

		private final ETownType town;
		private final EGradeType grade;
		private final StringBuilder message;
	}
}
