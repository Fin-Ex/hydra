package sf.l2j.gameserver.network.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;

import sf.l2j.L2DatabaseFactory;
import sf.l2j.gameserver.data.PlayerNameTable;
import sf.l2j.gameserver.model.World;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.FriendList;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestFriendDel extends L2GameClientPacket {

	private String _name;

	@Override
	protected void readImpl() {
		_name = readS();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		int id = PlayerNameTable.getInstance().getPlayerObjectId(_name);

		if (id == -1 || !activeChar.getFriendList().contains(id)) {
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_NOT_ON_YOUR_FRIENDS_LIST).addString(_name));
			return;
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
			PreparedStatement statement = con.prepareStatement("DELETE FROM character_friends WHERE (char_id = ? AND friend_id = ?) OR (char_id = ? AND friend_id = ?)");
			statement.setInt(1, activeChar.getObjectId());
			statement.setInt(2, id);
			statement.setInt(3, id);
			statement.setInt(4, activeChar.getObjectId());
			statement.execute();
			statement.close();

			// Player deleted from your friendlist
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_BEEN_DELETED_FROM_YOUR_FRIENDS_LIST).addString(_name));

			activeChar.getFriendList().remove(Integer.valueOf(id));
			activeChar.sendPacket(new FriendList(activeChar)); // update friendList *heavy method*

			Player player = World.getInstance().getPlayer(_name);
			if (player != null) {
				player.getFriendList().remove(Integer.valueOf(activeChar.getObjectId()));
				player.sendPacket(new FriendList(player)); // update friendList *heavy method*
			}
		} catch (Exception e) {
			_log.warn("could not delete friend objectid: ", e);
		}
	}
}
