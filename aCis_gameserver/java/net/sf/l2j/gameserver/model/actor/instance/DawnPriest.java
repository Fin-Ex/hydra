package net.sf.l2j.gameserver.model.actor.instance;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.instancemanager.SevenSigns;
import net.sf.l2j.gameserver.instancemanager.SevenSigns.CabalType;
import net.sf.l2j.gameserver.instancemanager.SevenSigns.SealType;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class DawnPriest extends SignsPriest {

	public DawnPriest(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command) {
		if (command.startsWith("Chat")) {
			showChatWindow(player);
		} else {
			super.onBypassFeedback(player, command);
		}
	}

	@Override
	public void showChatWindow(Player player) {
		player.sendPacket(ActionFailed.STATIC_PACKET);

		String filename = SevenSigns.SEVEN_SIGNS_HTML_PATH;

		final CabalType winningCabal = SevenSigns.getInstance().getCabalHighestScore();

		switch (SevenSigns.getInstance().getPlayerCabal(player.getObjectId())) {
			case DAWN:
				if (SevenSigns.getInstance().isCompResultsPeriod()) {
					filename += "dawn_priest_5.htm";
				} else if (SevenSigns.getInstance().isRecruitingPeriod()) {
					filename += "dawn_priest_6.htm";
				} else if (SevenSigns.getInstance().isSealValidationPeriod()) {
					if (winningCabal == CabalType.DAWN) {
						if (winningCabal != SevenSigns.getInstance().getSealOwner(SealType.GNOSIS)) {
							filename += "dawn_priest_2c.htm";
						} else {
							filename += "dawn_priest_2a.htm";
						}
					} else if (winningCabal == CabalType.NORMAL) {
						filename += "dawn_priest_2d.htm";
					} else {
						filename += "dawn_priest_2b.htm";
					}
				} else {
					filename += "dawn_priest_1b.htm";
				}
				break;

			case DUSK:
				if (SevenSigns.getInstance().isSealValidationPeriod()) {
					filename += "dawn_priest_3a.htm";
				} else {
					filename += "dawn_priest_3b.htm";
				}
				break;

			default:
				if (SevenSigns.getInstance().isCompResultsPeriod()) {
					filename += "dawn_priest_5.htm";
				} else if (SevenSigns.getInstance().isRecruitingPeriod()) {
					filename += "dawn_priest_6.htm";
				} else if (SevenSigns.getInstance().isSealValidationPeriod()) {
					if (winningCabal == CabalType.DAWN) {
						filename += "dawn_priest_4.htm";
					} else if (winningCabal == CabalType.NORMAL) {
						filename += "dawn_priest_2d.htm";
					} else {
						filename += "dawn_priest_2b.htm";
					}
				} else {
					filename += "dawn_priest_1a.htm";
				}
				break;
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
}
