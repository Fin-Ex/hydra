package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.Config;
import sf.l2j.gameserver.data.SkillTable;
import sf.l2j.gameserver.data.SkillTreeTable;
import sf.l2j.gameserver.instancemanager.FishingChampionshipManager;
import sf.l2j.gameserver.model.L2SkillLearn;
import sf.l2j.gameserver.model.actor.Npc;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.AcquireSkillList;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.L2Skill;

public class Fisherman extends Merchant {

	public Fisherman(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public String getHtmlPath(int npcId, int val) {
		String filename = "";

		if (val == 0) {
			filename = "" + npcId;
		} else {
			filename = npcId + "-" + val;
		}

		return "data/html/fisherman/" + filename + ".htm";
	}

	@Override
	public void onBypassFeedback(Player player, String command) {
		if (command.startsWith("FishSkillList")) {
			player.setSkillLearningClassId(player.getClassId());
			showFishSkillList(player);
		} else if (command.startsWith("FishingChampionship")) {
			if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED) {
				FishingChampionshipManager.getInstance().showChampScreen(player, getObjectId());
			} else {
				sendHtml(player, this, "no_fish_event001.htm");
			}
		} else if (command.startsWith("FishingReward")) {
			if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED) {
				if (FishingChampionshipManager.getInstance().isWinner(player.getName())) {
					FishingChampionshipManager.getInstance().getReward(player);
				} else {
					sendHtml(player, this, "no_fish_event_reward001.htm");
				}
			} else {
				sendHtml(player, this, "no_fish_event001.htm");
			}
		} else {
			super.onBypassFeedback(player, command);
		}
	}

	public static void showFishSkillList(Player player) {
		AcquireSkillList asl = new AcquireSkillList(AcquireSkillList.SkillType.Fishing);
		boolean empty = true;

		for (L2SkillLearn sl : SkillTreeTable.getInstance().getAvailableFishingDwarvenCraftSkills(player)) {
			L2Skill sk = SkillTable.getInstance().getInfo(sl.getId(), sl.getLevel());
			if (sk == null) {
				continue;
			}

			asl.addSkill(sl.getId(), sl.getLevel(), sl.getLevel(), sl.getSpCost(), 1);
			empty = false;
		}

		if (empty) {
			int minlevel = SkillTreeTable.getInstance().getMinLevelForNewFishingDwarvenCraftSkill(player);

			if (minlevel > 0) {
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN_S1).addNumber(minlevel));
			} else {
				player.sendPacket(SystemMessageId.NO_MORE_SKILLS_TO_LEARN);
			}
		} else {
			player.sendPacket(asl);
		}

		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	private static void sendHtml(Player player, Npc npc, String htmlName) {
		final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		html.setFile("data/html/fisherman/championship/" + htmlName);
		player.sendPacket(html);
	}
}
