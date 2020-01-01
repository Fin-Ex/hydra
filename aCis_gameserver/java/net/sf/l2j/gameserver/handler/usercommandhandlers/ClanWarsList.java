package net.sf.l2j.gameserver.handler.usercommandhandlers;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Support for /clanwarlist command
 *
 * @author Tempy
 */
public class ClanWarsList implements IHandler {

	private static final Integer[] COMMAND_IDS = {
		88,
		89,
		90
	};

	@Override
	public void invoke(Object... args) {
		final int id = (int) args[0];
		final Player activeChar = (Player) args[1];
		Clan clan = activeChar.getClan();
		if (clan == null) {
			activeChar.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
			PreparedStatement statement;

			// Attack List
			switch (id) {
			// Under Attack List
				case 88:
					statement = con.prepareStatement("SELECT clan_name,clan_id,ally_id,ally_name FROM clan_data,clan_wars WHERE clan1=? AND clan_id=clan2 AND clan2 NOT IN (SELECT clan1 FROM clan_wars WHERE clan2=?)");
					break;
			// War List
				case 89:
					statement = con.prepareStatement("SELECT clan_name,clan_id,ally_id,ally_name FROM clan_data,clan_wars WHERE clan2=? AND clan_id=clan1 AND clan1 NOT IN (SELECT clan2 FROM clan_wars WHERE clan1=?)");
					break;
				default:
					statement = con.prepareStatement("SELECT clan_name,clan_id,ally_id,ally_name FROM clan_data,clan_wars WHERE clan1=? AND clan_id=clan2 AND clan2 IN (SELECT clan1 FROM clan_wars WHERE clan2=?)");
					break;
			}

			statement.setInt(1, clan.getClanId());
			statement.setInt(2, clan.getClanId());

			ResultSet rset = statement.executeQuery();

			if (rset.first()) {
				switch (id) {
					case 88:
						activeChar.sendPacket(SystemMessageId.CLANS_YOU_DECLARED_WAR_ON);
						break;
					case 89:
						activeChar.sendPacket(SystemMessageId.CLANS_THAT_HAVE_DECLARED_WAR_ON_YOU);
						break;
					default:
						activeChar.sendPacket(SystemMessageId.WAR_LIST);
						break;
				}

				SystemMessage sm;
				while (rset.next()) {
					String clanName = rset.getString("clan_name");

					if (rset.getInt("ally_id") > 0) {
						sm = SystemMessage.getSystemMessage(SystemMessageId.S1_S2_ALLIANCE).addString(clanName).addString(rset.getString("ally_name"));
					} else {
						sm = SystemMessage.getSystemMessage(SystemMessageId.S1_NO_ALLI_EXISTS).addString(clanName);
					}

					activeChar.sendPacket(sm);
				}

				activeChar.sendPacket(SystemMessageId.FRIEND_LIST_FOOTER);
			} else {
				switch (id) {
					case 88:
						activeChar.sendPacket(SystemMessageId.YOU_ARENT_IN_CLAN_WARS);
						break;
					case 89:
						activeChar.sendPacket(SystemMessageId.NO_CLAN_WARS_VS_YOU);
						break;
					case 90:
						activeChar.sendPacket(SystemMessageId.NOT_INVOLVED_IN_WAR);
						break;
					default:
						break;
				}
			}

			rset.close();
			statement.close();
		} catch (Exception e) {
		}
	}

	@Override
	public Integer[] commands() {
		return COMMAND_IDS;
	}
}
