/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author finfan
 */
@Slf4j
public class AdminGLT implements IAdminCommandHandler {

	private static final String PATH = "D:\\glt_spawn.txt"; // spawn loca and chaotic spawn (second for traps, herbs and etc..) first for players after die

	private static final String[] COMMANDS = {
		"admin_glt_spawn1",
		"admin_glt_spawn2" //isChaoticSpawn
	};

	@Override
	public boolean useAdminCommand(String command, Player activeChar) {
		if (command.startsWith("admin_glt_spawn")) {
			try {
				final File f = new File(PATH);
				if(!f.exists()) {
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

		}

		return true;
	}

	@Override
	public String[] getAdminCommandList() {
		return COMMANDS;
	}

	private static class SpawnRecord {

		public int x, y, z;
		public boolean isChaotic;

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
}
