package net.sf.l2j.gameserver.handler.admincommandhandlers;


import java.util.StringTokenizer;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.data.MapRegionTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class AdminZone implements IAdminCommandHandler {

	private static final String[] ADMIN_COMMANDS = {
		"admin_zone_check",
		"admin_zone_visual"
	};

	@Override
	public boolean useAdminCommand(String command, Player activeChar) {
		if (activeChar == null) {
			return false;
		}

		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command

		if (actualCommand.equalsIgnoreCase("admin_zone_check")) {
			showHtml(activeChar);
		} else if (actualCommand.equalsIgnoreCase("admin_zone_visual")) {
			try {
				String next = st.nextToken();
				if (next.equalsIgnoreCase("all")) {
					for (L2ZoneType zone : ZoneManager.getInstance().getZones(activeChar)) {
						zone.visualizeZone(activeChar.getZ());
					}

					showHtml(activeChar);
				} else if (next.equalsIgnoreCase("clear")) {
					ZoneManager.getInstance().clearDebugItems();
					showHtml(activeChar);
				} else {
					int zoneId = Integer.parseInt(next);
					ZoneManager.getInstance().getZoneById(zoneId).visualizeZone(activeChar.getZ());
				}
			} catch (Exception e) {
				activeChar.sendMessage("Invalid parameter for //zone_visual.");
			}
		}

		return true;
	}

	private static void showHtml(Player activeChar) {
		int x = activeChar.getX();
		int y = activeChar.getY();
		int rx = (x - World.WORLD_X_MIN) / World.TILE_SIZE + World.TILE_X_MIN;
		int ry = (y - World.WORLD_Y_MIN) / World.TILE_SIZE + World.TILE_Y_MIN;

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/zone.htm");

		html.replace("%MAPREGION%", "[x:" + MapRegionTable.getMapRegionX(x) + " y:" + MapRegionTable.getMapRegionY(y) + "]");
		html.replace("%GEOREGION%", rx + "_" + ry);
		html.replace("%CLOSESTTOWN%", MapRegionTable.getInstance().getClosestTownName(x, y));
		html.replace("%CURRENTLOC%", x + ", " + y + ", " + activeChar.getZ());

		html.replace("%PVP%", (activeChar.isInsideZone(ZoneId.PVP) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%PEACE%", (activeChar.isInsideZone(ZoneId.PEACE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%SIEGE%", (activeChar.isInsideZone(ZoneId.SIEGE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%MOTHERTREE%", (activeChar.isInsideZone(ZoneId.MOTHER_TREE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%CLANHALL%", (activeChar.isInsideZone(ZoneId.CLAN_HALL) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%NOLANDING%", (activeChar.isInsideZone(ZoneId.NO_LANDING) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%WATER%", (activeChar.isInsideZone(ZoneId.WATER) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%JAIL%", (activeChar.isInsideZone(ZoneId.JAIL) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%MONSTERTRACK%", (activeChar.isInsideZone(ZoneId.MONSTER_TRACK) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%CASTLE%", (activeChar.isInsideZone(ZoneId.CASTLE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%SWAMP%", (activeChar.isInsideZone(ZoneId.SWAMP) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%NOSUMMONFRIEND%", (activeChar.isInsideZone(ZoneId.NO_SUMMON_FRIEND) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%NOSTORE%", (activeChar.isInsideZone(ZoneId.NO_STORE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%TOWN%", (activeChar.isInsideZone(ZoneId.TOWN) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%HQ%", (activeChar.isInsideZone(ZoneId.HQ) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%DANGERAREA%", (activeChar.isInsideZone(ZoneId.DANGER_AREA) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%CASTONARTIFACT%", (activeChar.isInsideZone(ZoneId.CAST_ON_ARTIFACT) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		html.replace("%NORESTART%", (activeChar.isInsideZone(ZoneId.NO_RESTART) ? "<font color=\"LEVEL\">YES</font>" : "NO"));

		final StringBuilder sb = new StringBuilder(100);
		for (L2ZoneType zone : World.getInstance().getRegion(x, y).getZones()) {
			if (zone.isCharacterInZone(activeChar)) {
				StringUtil.append(sb, zone.getId(), " ");
			}
		}
		html.replace("%ZLIST%", sb.toString());
		activeChar.sendPacket(html);
	}

	@Override
	public String[] getAdminCommandList() {
		return ADMIN_COMMANDS;
	}
}
