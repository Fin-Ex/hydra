package net.sf.l2j.gameserver.handler.usercommandhandlers;


import java.text.SimpleDateFormat;
import java.util.Map;
import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * Support for clan penalty user command.
 *
 * @author Tempy
 */
public class ClanPenalty implements IHandler {

	private static final String NO_PENALTY = "<tr><td width=170>No penalty is imposed.</td><td width=100 align=center></td></tr>";

	private static final Integer[] COMMAND_IDS = {
		100
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final StringBuilder sb = new StringBuilder();

		// Join a clan penalty.
		if (activeChar.getClanJoinExpiryTime() > System.currentTimeMillis()) {
			StringUtil.append(sb, "<tr><td width=170>Unable to join a clan.</td><td width=100 align=center>", sdf.format(activeChar.getClanJoinExpiryTime()), "</td></tr>");
		}

		// Create a clan penalty.
		if (activeChar.getClanCreateExpiryTime() > System.currentTimeMillis()) {
			StringUtil.append(sb, "<tr><td width=170>Unable to create a clan.</td><td width=100 align=center>", sdf.format(activeChar.getClanCreateExpiryTime()), "</td></tr>");
		}

		final Clan clan = activeChar.getClan();
		if (clan != null) {
			// Invitation in a clan penalty.
			if (clan.getCharPenaltyExpiryTime() > System.currentTimeMillis()) {
				StringUtil.append(sb, "<tr><td width=170>Unable to invite a clan member.</td><td width=100 align=center>", sdf.format(clan.getCharPenaltyExpiryTime()), "</td></tr>");
			}

			// War penalty.
			if (!clan.getWarPenalty().isEmpty()) {
				for (Map.Entry<Integer, Long> entry : clan.getWarPenalty().entrySet()) {
					if (entry.getValue() > System.currentTimeMillis()) {
						final Clan enemyClan = ClanTable.getInstance().getClan(entry.getKey());
						if (enemyClan != null) {
							StringUtil.append(sb, "<tr><td width=170>Unable to attack ", enemyClan.getName(), " clan.</td><td width=100 align=center>", sdf.format(entry.getValue()), "</td></tr>");
						}
					}
				}
			}
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/clan_penalty.htm");
		html.replace("%content%", (sb.length() == 0) ? NO_PENALTY : sb.toString());
		activeChar.sendPacket(html);
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
