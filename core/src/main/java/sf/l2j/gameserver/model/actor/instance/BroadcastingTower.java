package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.Player;
import java.util.StringTokenizer;

import sf.l2j.gameserver.instancemanager.CastleManager;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ItemList;

/**
 * @author NightMarez
 */
public final class BroadcastingTower extends Folk {

	public BroadcastingTower(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command) {
		if (command.startsWith("observe")) {
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();

			final int cost = Integer.parseInt(st.nextToken());
			final int x = Integer.parseInt(st.nextToken());
			final int y = Integer.parseInt(st.nextToken());
			final int z = Integer.parseInt(st.nextToken());

			if (command.startsWith("observeSiege") && CastleManager.getInstance().getSiege(x, y, z) == null) {
				player.sendPacket(SystemMessageId.ONLY_VIEW_SIEGE);
				return;
			}

			if (player.reduceAdena("Broadcast", cost, this, true)) {
				player.enterObserverMode(x, y, z);
				player.sendPacket(new ItemList(player, false));
			}
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

		return "data/html/observation/" + filename + ".htm";
	}
}
