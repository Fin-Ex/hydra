package net.sf.l2j.gameserver.model.actor.instance;

import java.util.Calendar;
import java.util.StringTokenizer;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.data.xml.TeleportLocationData;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.location.TeleportLocation;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public final class Gatekeeper extends Folk {

	private static final int COND_BUSY_BECAUSE_OF_SIEGE = 1;
	private static final int COND_OWNER = 2;
	private static final int COND_REGULAR = 3;

	public Gatekeeper(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command) {
		player.sendPacket(ActionFailed.STATIC_PACKET);

		if (command.startsWith("goto")) {
			final StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();

			if (st.countTokens() <= 0) {
				return;
			}

			final int condition = validateCondition(player);
			if (condition == COND_REGULAR || condition == COND_OWNER) {
				if (player.isAlikeDead()) {
					return;
				}

				final TeleportLocation list = TeleportLocationData.getInstance().getTeleportLocation(Integer.parseInt(st.nextToken()));
				if (list == null) {
					return;
				}

				final Siege siegeOnTeleportLocation = CastleManager.getInstance().getSiege(list.getX(), list.getY(), list.getZ());
				if (siegeOnTeleportLocation != null && siegeOnTeleportLocation.isInProgress()) {
					player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
					return;
				}

				if (!Config.KARMA_PLAYER_CAN_USE_GK && player.getKarma() > 0) {
					player.sendMessage("Go away, you're not welcome here.");
					return;
				}

				if (list.isNoble() && !player.isNoble()) {
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/teleporter/nobleteleporter-no.htm");
					html.replace("%objectId%", getObjectId());
					html.replace("%npcname%", getName());
					player.sendPacket(html);
					return;
				}

				Calendar cal = Calendar.getInstance();
				int price = list.getPrice();

				if (!list.isNoble()) {
					if (cal.get(Calendar.HOUR_OF_DAY) >= 20 && cal.get(Calendar.HOUR_OF_DAY) <= 23 && (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7)) {
						price /= 2;
					}
				}

				if (player.getInventory().getHunterCardInstance() != null) {
					price = player.getInventory().getHunterCardInstance().getTeleportPrice(price);
				}
				
				if (player.destroyItemByItemId("Teleport ", (list.isNoble()) ? 6651 : 57, price, this, true)) {
					player.teleToLocation(list, 20);
				}

				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
		} else if (command.startsWith("Chat")) {
			Calendar cal = Calendar.getInstance();
			int val = 0;
			try {
				val = Integer.parseInt(command.substring(5));
			} catch (IndexOutOfBoundsException | NumberFormatException ioobe) {
			}

			if (val == 1 && cal.get(Calendar.HOUR_OF_DAY) >= 20 && cal.get(Calendar.HOUR_OF_DAY) <= 23 && (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7)) {
				showHalfPriceHtml(player);
				return;
			}
			showChatWindow(player, val);
		} else {
			super.onBypassFeedback(player, command);
		}
	}

	@Override
	public String getHtmlPath(int npcId, int val) {
		String filename = "";
		if (val == 0) {
			filename = "" + npcId;
		} else {
			filename = npcId + "-" + val;
		}

		return "data/html/teleporter/" + filename + ".htm";
	}

	private void showHalfPriceHtml(Player player) {
		if (player == null) {
			return;
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());

		String content = HtmCache.getInstance().getHtm("data/html/teleporter/half/" + getNpcId() + ".htm");
		if (content == null) {
			content = HtmCache.getInstance().getHtmForce("data/html/teleporter/" + getNpcId() + "-1.htm");
		}

		html.setHtml(content);
		html.replace("%objectId%", getObjectId());
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}

	@Override
	public void showChatWindow(Player player) {
		String filename = "data/html/teleporter/castleteleporter-no.htm";

		int condition = validateCondition(player);
		if (condition == COND_REGULAR) {
			super.showChatWindow(player);
			return;
		}

		if (condition == COND_BUSY_BECAUSE_OF_SIEGE) {
			filename = "data/html/teleporter/castleteleporter-busy.htm";
		} else if (condition == COND_OWNER) {
			filename = getHtmlPath(getNpcId(), 0);
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}

	private int validateCondition(Player player) {
		if (getCastle() != null) {
			if (getCastle().getSiege().isInProgress()) {
				return COND_BUSY_BECAUSE_OF_SIEGE;
			}

			if (player.getClan() != null && getCastle().getOwnerId() == player.getClanId()) {
				return COND_OWNER;
			}
		}

		return COND_REGULAR;
	}
}
