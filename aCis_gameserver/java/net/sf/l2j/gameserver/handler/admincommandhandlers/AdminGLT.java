/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.model.zone.type.L2GLTBushZone;

/**
 *
 * @author finfan
 */
@Slf4j
public class AdminGLT implements IAdminCommandHandler {

	private static final String PATH = "D:\\glt_spawn.txt"; // spawn loca and chaotic spawn (second for traps, herbs and etc..) first for players after die
	private static final String PATH2 = "D:\\glt_bush.txt";
	private static final String PATH3 = "D:\\glt_coords.txt";

	private static final String[] COMMANDS = {
		"admin_glt_spawn1",
		"admin_glt_spawn2", //isChaoticSpawn=true
		"admin_glt_bush",
		"admin_glt_coords" //just save coords with heading
	};

	private int lastGLTBushZoneID = 0;

	public AdminGLT() {
		final Collection<L2GLTBushZone> zones = ZoneManager.getInstance().getAllZones(L2GLTBushZone.class);
		zones.forEach(zone -> {
			if(lastGLTBushZoneID < zone.getId()) {
				lastGLTBushZoneID = zone.getId();
			}
		});
		lastGLTBushZoneID++;
	}

	@Override
	public boolean useAdminCommand(String command, Player activeChar) {
		if (command.startsWith("admin_glt_spawn")) {
			try {
				final File f = new File(PATH);
				if (!f.exists()) {
					f.createNewFile();
				}

				try (FileWriter writer = new FileWriter(f, true)) {
					SpawnRecord record = new SpawnRecord();
					record.x = activeChar.getX();
					record.y = activeChar.getY();
					record.z = activeChar.getZ();
					if (command.endsWith("2")) {
						record.isChaotic = true;
					}
					writer.write(record.toString());
				}
			} catch (IOException e) {
				log.error("Error when record the file", e);
				return false;
			}
		} else if (command.startsWith("admin_glt_bush")) {
			final StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			final int radius = Integer.valueOf(st.nextToken());
			try {
				final File f = new File(PATH2);
				if (!f.exists()) {
					f.createNewFile();
				}

				try (FileWriter writer = new FileWriter(f, true)) {
					final Cylinder cylinder = new Cylinder();
					cylinder.id = lastGLTBushZoneID++;
					cylinder.radius = radius;
					cylinder.maxZ = activeChar.getZ() + 100;
					cylinder.minZ = activeChar.getZ() - 100;
					cylinder.x = activeChar.getX();
					cylinder.y = activeChar.getY();
					writer.write(cylinder.toString());
				}
			} catch (IOException e) {
				log.error("Error when record the file", e);
				return false;
			}
		} else if (command.startsWith("admin_glt_coords")) {
			try {
				final File f = new File(PATH3);
				if (!f.exists()) {
					f.createNewFile();
				}

				try (FileWriter writer = new FileWriter(f, true)) {
					Coords coords = new Coords();
					coords.x = activeChar.getX();
					coords.y = activeChar.getY();
					coords.z = activeChar.getZ();
					coords.heading = activeChar.getHeading();
					writer.write(coords.toCodeString());
				}
			} catch (IOException e) {
				log.error("Error when record the file", e);
				return false;
			}
		}

		return true;
	}

	@Override
	public String[] getAdminCommandList() {
		return COMMANDS;
	}

	private static class SpawnRecord {

		int x, y, z;
		boolean isChaotic;

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder().append("<spawn X=\"").append(x).append("\" ")
					.append("Y=\"").append(y).append("\" ")
					.append("Z=\"").append(z).append("\"");

			if (isChaotic) {
				sb.append(" isChaotic=\"").append(true).append("\" />\n");
			} else {
				sb.append(" />\n");
			}

			return sb.toString();
		}
	}

	private static class Cylinder {

		int id;
		int x, y;
		int minZ, maxZ;
		int radius;

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("\t<zone id=\"").append(id).append("\" type=\"GLTBushZone\" shape=\"Cylinder\" rad=\"")
					.append(radius).append("\" minZ=\"").append(minZ).append("\" maxZ=\"").append(maxZ).append("\">\n");
			sb.append("\t\t<node X=\"").append(x).append("\" Y=\"").append(y).append("\" />").append("\n\t</zone>\n");
			return sb.toString();
		}
	}
	
	private static class Coords {

		int x, y, z, heading;

		public SpawnLocation getSpawnLocation() {
			return new SpawnLocation(x, y, z, heading);
		}
		
		public String toCodeString() {
			return "new SpawnLocation(" + x + ", " + y + ", " + z + ", " + heading + "),\n";
		}
		
	}
}
