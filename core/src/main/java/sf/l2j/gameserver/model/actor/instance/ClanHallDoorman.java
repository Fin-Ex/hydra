package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.data.sql.ClanTable;
import sf.l2j.gameserver.instancemanager.ClanHallManager;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.model.entity.ClanHall;
import sf.l2j.gameserver.model.pledge.Clan;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class ClanHallDoorman extends Doorman {

	private ClanHall _clanHall;

	public ClanHallDoorman(int objectID, NpcTemplate template) {
		super(objectID, template);
	}

	@Override
	public void showChatWindow(Player player) {
		player.sendPacket(ActionFailed.STATIC_PACKET);

		if (_clanHall == null) {
			return;
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());

		final Clan owner = ClanTable.getInstance().getClan(_clanHall.getOwnerId());
		if (isOwnerClan(player)) {
			html.setFile("data/html/clanHallDoormen/doormen.htm");
			html.replace("%clanname%", owner.getName());
		} else {
			if (owner != null && owner.getLeader() != null) {
				html.setFile("data/html/clanHallDoormen/doormen-no.htm");
				html.replace("%leadername%", owner.getLeaderName());
				html.replace("%clanname%", owner.getName());
			} else {
				html.setFile("data/html/clanHallDoormen/emptyowner.htm");
				html.replace("%hallname%", _clanHall.getName());
			}
		}
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}

	@Override
	protected final void openDoors(Player player, String command) {
		_clanHall.openCloseDoors(true);

		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/clanHallDoormen/doormen-opened.htm");
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}

	@Override
	protected final void closeDoors(Player player, String command) {
		_clanHall.openCloseDoors(false);

		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/clanHallDoormen/doormen-closed.htm");
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}

	@Override
	protected final boolean isOwnerClan(Player player) {
		return _clanHall != null && player.getClan() != null && player.getClanId() == _clanHall.getOwnerId();
	}

	@Override
	public void onSpawn() {
		_clanHall = ClanHallManager.getInstance().getNearbyClanHall(getX(), getY(), 500);
		super.onSpawn();
	}
}
