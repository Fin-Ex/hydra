package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.finex.enums.EGradeType;
import net.sf.finex.enums.ETownType;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.instancemanager.SevenSigns;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ExQuestInfo;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author LBaldi
 */
public class Adventurer extends Folk {

	private final StringBuilder questBoard = new StringBuilder();

	public Adventurer(int objectId, NpcTemplate template) {
		super(objectId, template);
		questBoard.append("<br><a action=\"bypass -h npc_").append(getObjectId()).append("_questBoard\">").append("Town Quest Board</a>");
	}

	@Override
	public void onBypassFeedback(Player player, String command) {
		if (command.startsWith("raidInfo")) {
			int bossLevel = Integer.parseInt(command.substring(9).trim());
			String filename = "data/html/adventurer_guildsman/raid_info/info.htm";
			if (bossLevel != 0) {
				filename = "data/html/adventurer_guildsman/raid_info/level" + bossLevel + ".htm";
			}

			showChatWindow(player, filename);
		} else if (command.equalsIgnoreCase("questlist")) {
			player.sendPacket(ExQuestInfo.STATIC_PACKET);
		} else if (command.equalsIgnoreCase("questBoard")) {
			showChatWindow(player, "data/html/adventurer_guildsman/AboutQuestBoard.htm");
		} else if (command.equalsIgnoreCase("grades")) {
			showChatWindow(player, "data/html/adventurer_guildsman/AboutTownGrades.htm");
		} else {
			super.onBypassFeedback(player, command);
		}
	}

	@Override
	public void showChatWindow(Player player, int val) {
		final int npcId = getNpcId();
		String filename;
		
		if (npcId >= 31865 && npcId <= 31918)
			filename = SevenSigns.SEVEN_SIGNS_HTML_PATH + "rift/GuardianOfBorder.htm";
		else
			filename = getHtmlPath(npcId, val);
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		html.replace("%objectName%", getName());
		html.replace("%playerName%", player.getName());
		html.replace("%playerClass%", player.getClassId().name());
		html.replace("%questBoard%", Config.RANDOM_QUEST_GENERATOR_ON ? questBoard.toString() : "");
		player.sendPacket(html);

		// Send a Server->Client ActionFailed to the Player in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	@Override
	public void showChatWindow(Player player, String filename) {
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		html.replace("%objectName%", getName());
		html.replace("%playerName%", player.getName());
		html.replace("%playerClass%", player.getClassId().name());
		html.replace("%questBoard%", Config.RANDOM_QUEST_GENERATOR_ON ? questBoard.toString() : "");
		if (filename.contains("AboutQuestBoard")) {
			html.replace("%maxQuests%", Config.RANDOM_QUEST_COUNT_PER_GENERATION);
			html.replace("%updateTime%", Config.RANDOM_QUEST_RESET_TIME.toString());
		} else if (filename.contains("AboutTownGrades")) {
			final StringBuilder sb = new StringBuilder();
			sb.append("<table width=180>");
			int counter = 1;
			for(ETownType town : ETownType.VALUES) {
				sb.append("<tr>");
				sb.append("<td>").append(counter++).append(". ").append(town).append("</td>");
				for(EGradeType grade : town.getGrades()) {
					sb.append("<td width=10 align=center>");
					sb.append("<img src=\"Symbol.grade_").append(grade.name()).append("\" width=12 height=11>");
					sb.append("</td>");
				}
				sb.append("</tr>");
			}
			sb.append("</table>");
			html.replace("%townGradeTable%", sb.toString());
		}
		player.sendPacket(html);

		// Send a Server->Client ActionFailed to the Player in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	@Override
	public String getHtmlPath(int npcId, int val) {
		String filename = "";

		if (val == 0) {
			filename = "" + npcId;
		} else {
			filename = npcId + "-" + val;
		}

		return "data/html/adventurer_guildsman/" + filename + ".htm";
	}
}
