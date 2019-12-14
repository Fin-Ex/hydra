/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
@Slf4j
public class PlayerLineageDao {

	public static final void insert(Player player) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("INSERT INTO character_lineage (objectId,classIndex,levelReach,lineagePoints,resetPrice) VALUES (?,?,?,?,?)")) {
			st.setInt(1, player.getObjectId());
			st.setInt(2, player.getClassIndex());
			st.setInt(3, 0);
			st.setInt(4, 0);
			st.setInt(5, Config.TALENT_RESET_PRICE);
			st.executeUpdate();
		} catch (SQLException e) {
			log.error("", e);
		}
	}

	public static final void update(Player player) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("UPDATE character_lineage SET lineagePoints=?, levelReach=?, resetPrice=? WHERE objectId=? AND classIndex=?")) {
			st.setInt(1, player.getLineagePoints());
			st.setInt(2, player.getLineageReachLevel());
			st.setInt(3, player.getLineageResetPrice());
			st.setInt(4, player.getObjectId());
			st.setInt(5, player.getClassIndex());
			st.executeUpdate();
		} catch (SQLException e) {
			log.error("", e);
		}
	}

	public static final void delete(Player player) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("DELETE character_lineage WHERE objectId=?")) {
			st.setInt(1, player.getObjectId());
			st.executeUpdate();
		} catch (SQLException e) {
			log.error("", e);
		}
	}

	public static final void remove(Player player) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("DELETE character_lineage WHERE objectId=? AND classIndex=?")) {
			st.setInt(1, player.getObjectId());
			st.setInt(2, player.getClassIndex());
			st.executeUpdate();
		} catch (SQLException e) {
			log.error("", e);
		}
	}

	public static final void restore(Player player) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("SELECT * FROM character_lineage WHERE objectId=? AND classIndex=?")) {
			st.setInt(1, player.getObjectId());
			st.setInt(2, player.getClassIndex());
			try (ResultSet rset = st.executeQuery()) {
				while (rset.next()) {
					player.setLineageReachLevel(rset.getInt("levelReach"));
					player.setLineagePoints(rset.getInt("lineagePoints"));
					player.setLineageResetPrice(rset.getInt("resetPrice"));
				}
			}
		} catch (SQLException e) {
			log.error("", e);
		}
	}
}
