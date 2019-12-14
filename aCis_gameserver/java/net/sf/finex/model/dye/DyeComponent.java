/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.dye;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.AbstractComponent;
import net.sf.finex.data.DyeData;
import net.sf.finex.data.tables.DyeTable;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.HennaInfo;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;

/**
 *
 * @author FinFan
 */
@Slf4j
public class DyeComponent extends AbstractComponent {

	private static final String RESTORE_CHAR_HENNAS = "SELECT slot,symbol_id FROM character_hennas WHERE char_obj_id=? AND class_index=?";
	private static final String ADD_CHAR_HENNA = "INSERT INTO character_hennas (char_obj_id,symbol_id,slot,class_index) VALUES (?,?,?,?)";
	private static final String REMOVE_CHAR_HENNA = "DELETE FROM character_hennas WHERE char_obj_id=? AND slot=? AND class_index=?";
	private static final String DELETE_CHAR_HENNAS = "DELETE FROM character_hennas WHERE char_obj_id=? AND class_index=?";

	@Getter
	private final DyeData[] dyes = new DyeData[3];
	@Getter
	private int dyeSTR;
	@Getter
	private int dyeINT;
	@Getter
	private int dyeDEX;
	@Getter
	private int dyeMEN;
	@Getter
	private int dyeWIT;
	@Getter
	private int dyeCON;

	public DyeComponent(Player player) {
		super(player);
	}

	public int getEmptySlots() {
		int totalSlots = 0;
		if (getGameObject().getClassId().level() == 1) {
			totalSlots = 2;
		} else {
			totalSlots = 3;
		}

		for (int i = 0; i < 3; i++) {
			if (dyes[i] != null) {
				totalSlots--;
			}
		}

		if (totalSlots <= 0) {
			return 0;
		}

		return totalSlots;
	}

	public boolean removeDye(int index) {
		if (index > 2) {
			return false;
		}

		DyeData dye = dyes[index];
		dyes[index] = null;
		if (dye == null) {
			return false;
		}

		remove(index);
		recalculate();
		getGameObject().sendPacket(new HennaInfo(getGameObject()));
		getGameObject().sendPacket(new UserInfo(getGameObject()));
		getGameObject().reduceAdena("Henna", dye.getPrice() / 5, getGameObject(), false);
		getGameObject().addItem("Henna", dye.getDyeId(), DyeData.getRequiredDyeAmount() / 2, getGameObject(), true);
		getGameObject().sendPacket(SystemMessageId.SYMBOL_DELETED);
		return true;
	}

	/**
	 * Add a Henna to the Player, save update in the character_hennas table of
	 * the database and send Server->Client HennaInfo/UserInfo packet to this
	 * Player.
	 *
	 * @param dye The Henna template to add.
	 */
	public void addDye(DyeData dye) {
		for (int i = 0; i < 3; i++) {
			if (dyes[i] == null) {
				dyes[i] = dye;
				try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement(ADD_CHAR_HENNA)) {
					statement.setInt(1, getGameObject().getObjectId());
					statement.setInt(2, dye.getSymbolId());
					statement.setInt(3, i);
					statement.setInt(4, getGameObject().getClassIndex());
					statement.execute();
				} catch (Exception e) {
					log.error("Could not save char henna.", e);
				}
				recalculate();
				getGameObject().sendPacket(new HennaInfo(getGameObject()));
				getGameObject().sendPacket(new UserInfo(getGameObject()));
				getGameObject().sendPacket(SystemMessageId.SYMBOL_ADDED);
				return;
			}
		}
	}

	/**
	 * Calculate Henna modifiers of this Player.
	 */
	private void recalculate() {
		dyeINT = 0;
		dyeSTR = 0;
		dyeCON = 0;
		dyeMEN = 0;
		dyeWIT = 0;
		dyeDEX = 0;

		for (int i = 0; i < 3; i++) {
			if (dyes[i] == null) {
				continue;
			}

			dyeINT += dyes[i].getINT();
			dyeSTR += dyes[i].getSTR();
			dyeMEN += dyes[i].getMEN();
			dyeCON += dyes[i].getCON();
			dyeWIT += dyes[i].getWIT();
			dyeDEX += dyes[i].getDEX();
		}
	}

	public DyeData getDye(int slot) {
		try {
			return dyes[slot];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public boolean hasDye(int slot) {
		return dyes[slot] != null;
	}

	public DyeData setDye(DyeData data, int slot) {
		if (dyes[slot] == null) {
			dyes[slot] = data;
		}

		return dyes[slot];
	}

	@Override
	public void onAdd() {

	}

	@Override
	public void onRemove() {
	}

	@Override
	public void store() {
	}

	@Override
	public void restore() {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement(RESTORE_CHAR_HENNAS)) {
			statement.setInt(1, getGameObject().getObjectId());
			statement.setInt(2, getGameObject().getClassIndex());
			try (ResultSet rset = statement.executeQuery()) {
				for (int i = 0; i < 3; i++) {
					dyes[i] = null;
				}

				while (rset.next()) {
					int slot = rset.getInt("slot");
					int symbolId = rset.getInt("symbol_id");
					if (symbolId != 0) {
						final DyeData tpl = DyeTable.getInstance().get(symbolId);
						if (tpl != null) {
							dyes[slot] = tpl;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Could not restore henna.", e);
		}

		// Calculate Henna modifiers of this Player
		recalculate();
	}

	@Override
	public void delete() {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement(DELETE_CHAR_HENNAS)) {
			statement.setInt(1, getGameObject().getObjectId());
			statement.setInt(2, getGameObject().getClassIndex());
			statement.execute();
		} catch (Exception e) {
			log.error("Could not delete char dyes.", e);
		}
	}

	@Override
	public void remove(Object... args) {
		int slot = (int) args[0];
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement statement = con.prepareStatement(REMOVE_CHAR_HENNA)) {
			statement.setInt(1, getGameObject().getObjectId());
			statement.setInt(2, slot);
			statement.setInt(3, getGameObject().getClassIndex());
			statement.execute();
		} catch (Exception e) {
			log.error("Could not remove char dye.", e);
		}
	}

	@Override
	public Player getGameObject() {
		return super.getGameObject().getPlayer();
	}
}
