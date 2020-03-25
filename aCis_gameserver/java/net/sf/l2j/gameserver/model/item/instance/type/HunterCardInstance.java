/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.item.instance.type;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.enums.EGradeType;
import net.sf.finex.events.AbstractEventSubscription;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.events.OnKill;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.scripting.QuestState;

/**
 *
 * @author finfan
 */
@Slf4j
public class HunterCardInstance extends ItemInstance {
	
	private static final int[] EXP_TABLE = {
		100,
		800,
		1600,
		3200,
		6400
	};
	
	private AbstractEventSubscription<OnKill> event;
	private int payment;
	
	public byte level = 1;
	public int exp;
	public int huntID;
	public int count;
	public int current;
	public long timestamp;
	public int credits;

	public HunterCardInstance(int objectId, int itemId) {
		super(objectId, itemId);
	}

	public void refresh(Player hunter) {
		//TODO: restore stats of that item
		restore(hunter.getObjectId());
		hunter.addSkill(SkillTable.getInstance().getInfo(5099, level + 1));
	}

	public void getQuest(Player hunter) {
		if(huntID > 0) {
			log.info("Already has a quest!");
			return;
		}
		
		if (timestamp > System.currentTimeMillis()) {
			log.info("You have a penalty");
			return;
		}

		// give hunt quest
		final Collection<NpcTemplate> templates = World.getInstance().getNpcTemplates().keySet();
		final List<NpcTemplate> sortedList = new ArrayList<>();
		templates.stream().filter(next -> next.isType("Monster") && next.getRewardExp() > 0 && next.getRewardSp() > 0).forEachOrdered(next -> sortedList.add(next));
		Collections.sort(sortedList, Comparator.comparing(NpcTemplate::getLevel).reversed());

		final int[] hunterLevel = {
			hunter.getLevel() - 5,
			hunter.getLevel() + 5
		};

		List<NpcTemplate> tempList = sortedList;
		while (true) {
			int index = tempList.size() / 2 - 1;
			try {
				final NpcTemplate temp = tempList.get(index);
				final boolean monsterLevelIsInrange = temp.getLevel() >= hunterLevel[0] && temp.getLevel() <= hunterLevel[1];
				if (monsterLevelIsInrange) {
					huntID = temp.getNpcId();
					_log.info("Monster for hunt: {}[{}] setted.", temp.getName(), huntID);
					break;
				}

				final boolean monsterLevelLowerThanHunter = temp.getLevel() < hunterLevel[0];
				if (monsterLevelLowerThanHunter) {
					// set new list with new size
					tempList = new ArrayList<>(index + 1);
					tempList.addAll(index, sortedList);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}

		count = hunter.getLevel() / 5 * 10 + 40;
		_log.info("Hunt monsters count: {}", count);
		payment = (int) ((Math.pow(52, hunter.getLevel() / 100. + 1) * count) + (6000 * hunter.getLevel()));
		_log.info("Payment equals: {} credits (HC)", payment);
		timestamp = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
		_log.info("Timestamp on mini-hunt quest setted. Next take date: {}", new Date(timestamp));
		event = hunter.getEventBus().subscribe().cast(OnKill.class).forEach(this::onKill);
	}
	
	private void onKill(OnKill event) {
		if (!event.getVictim().isNpc() || huntID == 0) {
			return;
		}

		final Npc npc = event.getVictim().getNpc();
		if (npc.getNpcId() == huntID) {
			current++;
			if (current == count) {
				event.getKiller().sendMessage("Hunter Quest is complete!");
				event.getKiller().sendPacket(new PlaySound(QuestState.SOUND_FINISH));
				event.getKiller().getEventBus().unsubscribe(this.event);
				increaseExp(event.getKiller().getPlayer());
				credits += payment;
				huntID = 0;
				current = 0;
				count = 0;
			} else {
				event.getKiller().sendMessage("~ Hunter Quest (Long Hunt!): " + count + "/100");
				event.getKiller().sendPacket(new PlaySound(QuestState.SOUND_ITEMGET));
			}
		}
	}
	
	private void increaseExp(Player player) {
		if(level == EXP_TABLE.length) {
			return;
		}
		
		exp += 100;
		player.sendMessage("You receive " + 100 + " hunter rank experience!");
		
		if (exp >= EXP_TABLE[level - 1]) {
			// calc remains of exp
			exp = exp - EXP_TABLE[level - 1];

			// icnrease level
			level++;
			player.sendMessage("Hunter Rank was increased!");
			ThreadPool.schedule(() -> {
				player.sendPacket(new PlaySound(QuestState.SOUND_FANFARE));
			}, 1500);
		}
	}
	
	private EGradeType getRank() {
		return EGradeType.VALUES[level - 1];
	}
	
	public final int getTeleportPrice(int price) {
		return (int) (price * (1 - getRank().getId() * 10 + 10 / 100.));
	}

	/**
	 * TODO: oinsert to NPC
	 * @param price
	 * @return 
	 */
	public final int getConsumablePrice(int price) {
		return (int) (price * (1 - getRank().getId() * 5 + 5 / 100.));
	}
	
	private long setAndGetTimestamp() {
		return timestamp = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(28);
	}

	public void insert() {
		final StringBuilder query = new StringBuilder("INSERT items_hxh (hunterId=");
		query.append(ownerId).append(",");
		for(Field field : getClass().getFields()) {
			field.setAccessible(true);
			try {
				if(field.getName().equalsIgnoreCase("timestamp")) {
					query.append(field.getName()).append("=").append(setAndGetTimestamp());
				} else {
					query.append(field.getName()).append("=").append(field.get(this)).append(",");
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		query.deleteCharAt(query.length() - 1);
		query.append(")");
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement(query.toString())) {
			st.execute();
		} catch (SQLException e) {
			log.error("", e);
		}
		
		log.info("Hunter card getted by {}! And is valid until: {}", ownerId, new Date(timestamp));
	}
	
	public void update() {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("UPDATE items_hxh SET timestamp=? WHERE hunterId=?")) {
			st.setLong(1, setAndGetTimestamp());
			st.setInt(2, ownerId);
			st.execute();
		} catch (SQLException e) {
			log.error("", e);
		}
		
		log.info("Hunter card UPDATED and is valid until: {}!", new Date(timestamp));
	}
	
	private void restore(int objectId) {
		try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement st = con.prepareStatement("SELECT * FROM items_hxh WHERE hunterId=?")) {
			st.setInt(1, objectId);
			try (ResultSet rset = st.executeQuery()) {
				while(rset.next()) {
					level = rset.getByte("level");
					exp = rset.getInt("exp");
					huntID = rset.getInt("huntID");
					count = rset.getInt("count");
					payment = rset.getInt("payment");
					current = rset.getInt("current");
					credits = rset.getInt("credits");
					timestamp = rset.getLong("timestamp");
				}
			}
		} catch (SQLException e) {
			log.error("", e);
		}
		
		if(timestamp < System.currentTimeMillis()) {
			timestamp = 0;
			log.info("Hunter card is not valid anymore!");
		} else {
			log.info("Hunter card is valid until: {}!", new Date(timestamp));
		}
		
		if(huntID > 0) {
			event = getPlayer().getEventBus().subscribe().cast(OnKill.class).forEach(this::onKill);
		}
	}
	
	public boolean isValid() {
		return timestamp != 0 && timestamp > System.currentTimeMillis();
	}
	
	public final void showInfo(Player player) {
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/hunter_card.htm");
		html.replace("%level%", getRank().getNormalName());
		html.replace("%exp%", exp);
		html.replace("%target%", NpcTable.getInstance().getTemplate(huntID).getName());
		html.replace("%count%", count);
		html.replace("%current%", current);
		html.replace("%timestamp%", new Date(timestamp).toString());
		html.replace("%credits%", credits);
		html.replace("%mexp%", EXP_TABLE[level]);
		player.sendPacket(html);
	}
}
