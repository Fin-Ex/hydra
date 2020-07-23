/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.data.QuestRewardData;
import net.sf.finex.data.RandomQuestData;
import net.sf.finex.enums.EGradeType;
import net.sf.finex.enums.ERandomQuestType;
import net.sf.finex.enums.ETownType;
import net.sf.finex.events.EventBus;
import net.sf.finex.model.generator.quest.builder.DeliverItemBuilder;
import net.sf.l2j.Config;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.events.OnLogout;
import net.sf.l2j.gameserver.model.actor.instance.ClassMaster;
import net.sf.l2j.gameserver.model.actor.instance.Fisherman;
import net.sf.l2j.gameserver.model.actor.instance.Folk;
import net.sf.l2j.gameserver.model.actor.instance.Gatekeeper;
import net.sf.l2j.gameserver.model.actor.instance.Guard;
import net.sf.l2j.gameserver.model.actor.instance.ManorManagerNpc;
import net.sf.l2j.gameserver.model.actor.instance.Merchant;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.instance.RaceManagerNpc;
import net.sf.l2j.gameserver.model.actor.instance.RaidBoss;
import net.sf.l2j.gameserver.model.actor.instance.SymbolMaker;
import net.sf.l2j.gameserver.model.actor.instance.Trainer;
import net.sf.l2j.gameserver.model.actor.instance.VillageMaster;
import net.sf.l2j.gameserver.model.actor.instance.VillageMasterDElf;
import net.sf.l2j.gameserver.model.actor.instance.VillageMasterDwarf;
import net.sf.l2j.gameserver.model.actor.instance.VillageMasterFighter;
import net.sf.l2j.gameserver.model.actor.instance.VillageMasterMystic;
import net.sf.l2j.gameserver.model.actor.instance.VillageMasterOrc;
import net.sf.l2j.gameserver.model.actor.instance.VillageMasterPriest;
import net.sf.l2j.gameserver.model.actor.instance.WarehouseKeeper;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Broadcast;

/**
 *
 * @author FinFan
 */
@Slf4j
public class RandomQuestManager implements Runnable {

	@Getter private static final RandomQuestManager instance = new RandomQuestManager();
	@Getter private final Map<ETownType, Map<EGradeType, List<RandomQuestData>>> holder = new HashMap<>();
	@Getter private final EventBus eventBus = new EventBus();

	// Monster holders
	@Getter private final Map<Integer, List<NpcTemplate>> bosses = new HashMap<>();
	@Getter private final Map<Integer, List<NpcTemplate>> monsters = new HashMap<>();
	@Getter private final List<NpcTemplate> npcs = new ArrayList<>();

	private final ReentrantLock locker = new ReentrantLock();
	private LocalDateTime nextGeneration;

	public RandomQuestManager() {
		// set new next generate time
		nextGeneration = LocalDateTime.of(LocalDate.now(), Config.RANDOM_QUEST_RESET_TIME);
		if (LocalDateTime.now().isAfter(nextGeneration)) {
			nextGeneration = nextGeneration.plusDays(1);
		}

		// create all collections
		collectNpcs();
		eventBus.subscribe().cast(OnLogout.class).forEach(this::onLogout);
		log.info("Next re-generate quests will be in {}.", nextGeneration);
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}

	private void onLogout(OnLogout e) {
		final RandomQuestComponent component = e.getPlayer().getComponent(RandomQuestComponent.class);
		if (component.hasQuest()) {
			cancelQuest(e.getPlayer());
		}
	}

	public boolean isEmpty() {
		return holder.isEmpty();
	}

	public RandomQuestData getQuest(ETownType town, EGradeType grade, int id) {
		for (RandomQuestData q : holder.get(town).get(grade)) {
			if (q.getId() == id) {
				return q;
			}
		}

		return null;
	}

	/**
	 * Generating random quests by RandomQuestGenerator.class and add generated
	 * quests to the boards by HTML text. Every
	 * Config.RANDOM_QUEST_TIME_GENERATE quests will be generated every day. All
	 * generated quests inserts to holder and will be actived for next 24 hourse
	 * until new generation
	 */
	public void generateQuests() {
		locker.lock();
		try {
			holder.clear();

			int questCount = 0;
			for (ETownType town : ETownType.VALUES) {
				holder.put(town, new HashMap<>());
				for (EGradeType grade : town.getGrades()) {
					holder.get(town).put(grade, new ArrayList<>());
					for (int i = 0; i < Config.RANDOM_QUEST_COUNT_PER_GENERATION; i++) {
						ERandomQuestType type;
						for (type = Rnd.get(ERandomQuestType.values());; type = Rnd.get(ERandomQuestType.values())) {
							if (type == ERandomQuestType.Boss_Hunt && grade == EGradeType.NG) {
								continue;
							}
							break;
						}

						final RandomQuestData quest = type.create(type, town, grade);
						holder.get(town).get(grade).add(quest);
						questCount++;
					}
				}
			}
			RandomQuestHtmlManager.getInstance().build();
			log.info("Generated {} random quests for {} towns.", questCount, ETownType.VALUES.length);
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Give the generated quest from holder to player. Player at one moment can
	 * take only 1 quest mission with quest special timer.
	 *
	 * @param quest random quest data for giving
	 * @param player who take the quest
	 * @param npc a quest board
	 */
	public void addQuest(RandomQuestData quest, Player player, Npc npc) {
		if (quest.getOwnerId() > 0) {
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ALREADY_TAKENED_BY_ANOTHER_PERSON).addString(quest.getName()));
			return;
		}

		if (quest.getGrade().getMinLevel() > player.getLevel()) {
			player.sendPacket(SystemMessageId.YOUR_LEVEL_TO_LOW);
			return;
		}

		if (quest.getGrade().getMaxLevel() < player.getLevel()) {
			player.sendPacket(SystemMessageId.YOUR_LEVEL_TO_HIGH);
			return;
		}

		final RandomQuestComponent component = player.getComponent(RandomQuestComponent.class);
		if (component.hasPenalty()) {
			player.sendPacket(SystemMessageId.YOU_HAVE_A_PENALTY_FOR_TAKE_A_QUEST);
			return;
		}

		if (quest.getType() == ERandomQuestType.Item_Deliver) {
			int weightQuestItems = quest.getCondition().getValue() * ItemTable.getInstance().getTemplate(DeliverItemBuilder.DELIVER_ITEM_ID).getWeight();
			if (!player.getInventory().validateWeight(weightQuestItems)) {
				player.sendMessage("Your weight is too low for taking this quest.");
				return;
			}
		}

		locker.lock();
		try {
			if (quest.getType().getHandler().onGetQuest(quest, player, npc)) {
				quest.setOwnerId(player.getObjectId());
				quest.setDone(false);
				quest.setCounter(0);
				quest.setBoardId(npc.getNpcId());
				component.setQuest(quest);
				RandomQuestHtmlManager.getInstance().buildQuestList();
				player.sendPacket(new PlaySound("ItemSound.quest_accept"));
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.QUEST_S1_WAS_ACCEPTED).addString(quest.getName()));
			}
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Cancel's quest from player and return it to holder
	 *
	 * @param player
	 */
	public void cancelQuest(Player player) {
		locker.lock();
		try {
			final RandomQuestComponent component = player.getComponent(RandomQuestComponent.class);
			final RandomQuestData quest = component.getQuest();
			if (quest == null) {
				player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_A_QUEST);
				return;
			}

			quest.setOwnerId(0);
			quest.setCounter(0);
			quest.setDone(false);
			if (quest.getQuestItems() != null) {
				for (IntIntHolder itemHolder : quest.getQuestItems()) {
					final ItemInstance inst = player.getInventory().getItemByItemId(itemHolder.getId());
					if (player.getInventory().getItemByItemId(itemHolder.getId()) != null) {
						player.getInventory().destroyItem("QuestCancel", inst, player, null);
					}
				}
			}
			component.setQuest(null);
			component.setTimeStamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
			RandomQuestHtmlManager.getInstance().buildQuestList();
			player.sendPacket(new PlaySound("ItemSound.quest_giveup"));
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.QUEST_S1_WAS_CANCELED).addString(quest.getName()));
			log.info("Quest {} was canceled.");
		} finally {
			locker.unlock();
		}
	}

	public void completeQuest(Player player, Npc questBoard) {
		locker.lock();
		try {
			final RandomQuestData quest = player.getComponent(RandomQuestComponent.class).getQuest();
			if (quest.getExp() > 0 || quest.getSp() > 0) {
				player.addExpAndSp(quest.getExp(), quest.getSp());
			}

			if (quest.getRewards() != null && !quest.getRewards().isEmpty()) {
				List<ItemInstance> rewards = new ArrayList<>();
				for (QuestRewardData rewdata : quest.getRewards()) {
					rewards.add(ItemTable.getInstance().createItem("RandomQuestReward", rewdata.getId(), rewdata.getCount(), player, null));
				}

				if (!rewards.isEmpty()) {
					for (ItemInstance inst : rewards) {
						player.addItem("RandomQuestReward", inst, null, true);
					}
				}
			}

			// delete quest from player and from manager
			final RandomQuestComponent component = player.getComponent(RandomQuestComponent.class);
			holder.get(quest.getTown()).get(quest.getGrade()).remove(quest);
			component.setQuest(null);
			RandomQuestHtmlManager.getInstance().buildQuestList();

			final NpcHtmlMessage html = new NpcHtmlMessage(questBoard != null ? questBoard.getObjectId() : 0);
			html.setHtml("<html><title>Quest Board</title><body><br>Congradulations!<br1>You have successfully completed the quest!</body></html>");
			player.sendPacket(html);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.QUEST_S1_WAS_COMPLETED).addString(quest.getName()));
			player.sendPacket(ActionFailed.STATIC_PACKET);
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Collect all npcs
	 */
	private void collectNpcs() {
		for (NpcTemplate temp : NpcTable.getInstance().getAllNpcs()) {
			final int level = temp.getLevel();

			// Collect monsters
			if (temp.isType(Monster.class.getSimpleName()) && !temp.getTitle().startsWith("Quest") && temp.getRewardExp() > 0 && temp.getRewardSp() > 0) {
				List<NpcTemplate> list = monsters.get(level);
				if (list == null) {
					monsters.put(level, list = new ArrayList<>());
				}
				list.add(temp);
			}

			// collect raid bosses
			if (temp.isType(RaidBoss.class.getSimpleName())) {
				List<NpcTemplate> list = bosses.get(level);
				if (!bosses.containsKey(level)) {
					bosses.put(level, list = new ArrayList<>());
				}

				list.add(temp);
			}

			if (temp.isType(ClassMaster.class.getSimpleName())
					|| temp.isType(Fisherman.class.getSimpleName())
					|| temp.isType(Folk.class.getSimpleName())
					|| temp.isType(Gatekeeper.class.getSimpleName())
					|| temp.isType(Guard.class.getSimpleName())
					|| temp.isType(ManorManagerNpc.class.getSimpleName())
					|| temp.isType(Merchant.class.getSimpleName())
					|| temp.isType(RaceManagerNpc.class.getSimpleName())
					|| temp.isType(SymbolMaker.class.getSimpleName())
					|| temp.isType(Trainer.class.getSimpleName())
					|| temp.isType(VillageMaster.class.getSimpleName())
					|| temp.isType(VillageMasterDElf.class.getSimpleName())
					|| temp.isType(VillageMasterDwarf.class.getSimpleName())
					|| temp.isType(VillageMasterFighter.class.getSimpleName())
					|| temp.isType(VillageMasterMystic.class.getSimpleName())
					|| temp.isType(VillageMasterOrc.class.getSimpleName())
					|| temp.isType(VillageMasterPriest.class.getSimpleName())
					|| temp.isType(WarehouseKeeper.class.getSimpleName())) {
				switch (temp.getRace()) {
					case HUMAN:
					case ORC:
					case ELVE:
					case DWARVE:
					case DARKELVE:
						npcs.add(temp);
						break;
				}
			}
		}
	}

	/**
	 * TODO: remove
	 * @return 
	 */
	@Deprecated
	public boolean isLocked() {
		return locker.isLocked();
	}

	@Override
	public void run() {
		final LocalDateTime ldt = LocalDateTime.now();
		if (ldt.isAfter(nextGeneration)) {
			log.info("Start Quest Re-Generating...");
			nextGeneration = nextGeneration.plusDays(1);
			generateQuests();
			Broadcast.announceToOnlinePlayers("Town Quest Board's System: All quests was refreshed. New Quests awaits you in a Town Quest Board!");
		}
	}
}
