/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.data.tables;

import it.sauronsoftware.cron4j.Scheduler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sf.finex.data.RankerData;
import sf.l2j.L2DatabaseFactory;
import sf.l2j.commons.concurrent.ThreadPool;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.base.ClassId;
import sf.l2j.gameserver.model.base.ClassRace;
import sf.l2j.gameserver.model.base.Sex;

/**
 *
 * @author finfan
 */
@Slf4j
public final class GladiatorRankTable {

	private static final String PATTERN = "0 6 * * 1"; // “At 06:00 on Monday.”

	@Getter
	private static final GladiatorRankTable instance = new GladiatorRankTable();
	@Getter
	private final List<RankerData> rankers = new CopyOnWriteArrayList<>();

	private GladiatorRankTable() {
		restore();
		final Scheduler sc = new Scheduler();
		sc.schedule(PATTERN, () -> {
			recalculate();
		});
		sc.start();

		final long ms = TimeUnit.MINUTES.toMillis(5);
		ThreadPool.scheduleAtFixedRate(() -> {
			update();
		}, ms, ms);

		log.info("::GladiatorRankTable:: scheduler is started.");
		log.info("Next period of points renew is: {}", sc.getSchedulingPattern(PATTERN));
		log.info("Current ranker's: {}", rankers.size());
	}

	public boolean isOneOfTen(Player player) {
		try {
			for (int i = 0; i < 10; i++) {
				if (rankers.get(i).getObjectId() == player.getObjectId()) {
					return true;
				}
			}
			return false;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public RankerData get(Player player) {
		for (RankerData next : rankers) {
			if (next.getObjectId() == player.getObjectId()) {
				return next;
			}
		}
		return null;
	}

	public void increment(Player player, int value) {
		final RankerData data = get(player);
		if (data != null) {
			data.setPoints(data.getPoints() + value);
			player.sendMessage("Received " + value + " rank points.");
		}
		sort(this, true);
		if (isOneOfTen(player)) {
			player.sendMessage("You are the one of ten.");
		}
		player.sendMessage("Gladiator ranking: " + get(player).getPoints());
	}

	public void decrement(Player player, int value) {
		final RankerData data = get(player);
		if (data != null) {
			data.setPoints(data.getPoints() - value);
			player.sendMessage("Losted " + value + " rank points.");
		}
		final boolean oldStatus = isOneOfTen(player);
		sort(this, true);
		if (oldStatus && !isOneOfTen(player)) {
			player.sendMessage("You are not one of ten.");
		}
		player.sendMessage("Gladiator ranking: " + get(player).getPoints());
	}

	public void recalculate() {
		log.info("Start recalculating of first ten of gladiators...");
		sort(ClassId.class, true);
		try {
			for (int i = 0; i < 10; i++) {
				final RankerData data = rankers.remove(i);
				remove(data.getObjectId());
				log.info("Removed: {}", data);
			}
		} catch (IndexOutOfBoundsException e) {
			log.info("first ten was cleared. New first ten setted.");
		}
	}

	public <Type> void sort(Type type, boolean reversed) {
		log.info("sort by reverse for getting best glaiators");
		final Comparator<RankerData> comparing;
		if (type == ClassRace.class) {
			comparing = Comparator.comparing(RankerData::getRaceId);
		} else if (type == ClassId.class) {
			comparing = Comparator.comparing(RankerData::getClassId);
		} else if (type == Sex.class) {
			comparing = Comparator.comparing(RankerData::getSexId);
		} else {
			comparing = Comparator.comparing(RankerData::getPoints);
		}
		if (reversed) {
			comparing.reversed();
		}
		rankers.sort(comparing);
	}

	private void restore() {
		final String query = "SELECT * FROM class_ranks WHERE classId=?";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement(query)) {
			st.setInt(1, ClassId.Gladiator.getId());
			try (ResultSet rset = st.executeQuery()) {
				while (rset.next()) {
					final RankerData data = new RankerData(rset.getInt("objectId"));
					data.setClassId(ClassId.Gladiator.getId());
					data.setPoints(rset.getShort("points"));
					data.setRaceId(rset.getShort("raceId"));
					data.setSexId(rset.getShort("sexId"));
					rankers.add(data);
				}
			}
		} catch (SQLException e) {
			log.error("Cant restore class_ranks.", e);
		}
	}

	public void insert(Player player) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("INSERT INTO class_ranks (objectId,classId,raceId,sexId,points) VALUES (?,?,?,?,?)")) {
			st.setInt(1, player.getObjectId());
			st.setInt(2, ClassId.Gladiator.getId());
			st.setInt(3, player.getRace().ordinal());
			st.setInt(4, player.getAppearance().getSex().ordinal());
			st.setInt(5, 0);
			st.execute();
		} catch (SQLException e) {
			log.error("Cant insert class_ranks.", e);
		}
		rankers.add(new RankerData(player));
	}

	private void update() {
		final String query = "UPDATE class_ranks SET points=? WHERE objectId=? AND classId=?";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement(query)) {
			for (RankerData next : rankers) {
				st.setInt(1, next.getPoints());
				st.setInt(2, next.getObjectId());
				st.setInt(3, ClassId.Gladiator.getId());
				st.executeUpdate();
			}
		} catch (SQLException e) {
			log.error("Error in update gladiator record.", e);
		}
	}

	public void remove(int objectId) {
		final String query = "DELETE FROM class_ranks WHERE objectId=?";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement(query)) {
			st.setInt(1, objectId);
			st.executeUpdate();
		} catch (SQLException e) {
			log.error("Error in remove gladiator rank record for {}.", objectId, e);
		}
	}
}
