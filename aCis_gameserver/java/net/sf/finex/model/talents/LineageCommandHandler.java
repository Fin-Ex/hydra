/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.talents;

import lombok.extern.slf4j.Slf4j;
import net.sf.finex.data.TalentBranchData;
import net.sf.finex.data.TalentData;
import net.sf.finex.data.tables.TalentBranchTable;
import net.sf.finex.data.tables.TalentTable;
import net.sf.finex.handlers.dialog.DlgManager;
import net.sf.finex.handlers.dialog.requests.TalentLearnRequest;
import net.sf.finex.handlers.dialog.requests.TalentResetRequest;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 *
 * @author FinFan
 */
@Slf4j
public class LineageCommandHandler {

	public static final void showTalentList(Player player) {
		if (player.isLocked()) {
			player.sendPacket(SystemMessageId.S1_CANNOT_BE_USED);
			return;
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		final StringBuilder talentsHTML = TalentBranchTable.getInstance().getTalentsHtml(player.getClassId());
		if (talentsHTML == null) {
			html.setHtml("<html><body>Coming soon...</body></html>");
		} else {
			html.setHtml(talentsHTML.toString());
			html.replace("%points%", player.getLineagePoints() + " LP (Lineage Points)");
		}
		player.sendPacket(html);
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	public static final void showTalentInfo(Player player, int talentId) {
		if (player.isLocked()) {
			player.sendPacket(SystemMessageId.ACCESS_FAILED);
			return;
		}

		final TalentData talent = TalentTable.getInstance().get(talentId);
		if (talent == null) {
			return;
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/talents/info.htm");
		final L2Skill skill = SkillTable.getInstance().getInfo(talent.getSkillId(), 1);
		if (skill != null) {
			html.replace("%skillInfo%", skill.info());
		} else {
			html.replace("%skillInfo%", "");
			log.warn("Skill with ID[{}] doesnt exist!", talent.getSkillId());
		}
		html.replace("%name%", talent.getName());
		html.replace("%descr%", talent.getDescr());
		html.replace("%icon%", talent.getIcon());

		final int reqLvl = talent.getRequiredLevel();
		html.replace("%reqlvl%", reqLvl > 0 ? String.valueOf(reqLvl) : "40");

		final int reqTalent = talent.getRequiredTalent();
		html.replace("%reqtalent%", reqTalent > 0 ? TalentTable.getInstance().get(reqTalent).getName() : "None");

		html.replace("%status%", player.hasTalent(talentId) ? "<font color=00ff00>Learned</font>" : "<font color=ff0000>Not learned</font>");

		html.replace("%learn%", "<a action=\"bypass -h talentLearn " + talentId + "\">Learn</a>");
		player.sendPacket(html);
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	public static final void buttonTalentLearn(Player player, int talentId) {
		if (player.isLocked()) {
			player.sendPacket(SystemMessageId.S1_CANNOT_BE_USED);
			return;
		}

		final TalentBranchData branch = TalentBranchTable.getInstance().getBranch(player.getClassId());
		if (!player.getClassId().equalsOrChildOf(branch.getClassId())) {
			return;
		}

		final TalentData talent = branch.getTalent(talentId);
		if (talent == null) {
			return;
		}

		DlgManager.getInstance().getRequest(TalentLearnRequest.class).handle(player, talent);
	}

	public static final void buttonResetTalents(Player player) {
		DlgManager.getInstance().getRequest(TalentResetRequest.class).handle(player);
	}
}
