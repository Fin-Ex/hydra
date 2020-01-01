package net.sf.l2j.gameserver;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.FinexLoader;
import net.sf.finex.data.tables.DyeTable;
import net.sf.finex.data.tables.QuestDataTable;
import net.sf.finex.events.EventBus;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.mmocore.SelectorConfig;
import net.sf.l2j.commons.mmocore.SelectorThread;
import net.sf.l2j.commons.util.SysUtil;
import net.sf.l2j.gameserver.cache.CrestCache;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.communitybbs.Manager.ForumsBBSManager;
import net.sf.l2j.gameserver.data.BufferTable;
import net.sf.l2j.gameserver.data.CharTemplateTable;
import net.sf.l2j.gameserver.data.DoorTable;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.MapRegionTable;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.data.PlayerNameTable;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.SkillTreeTable;
import net.sf.l2j.gameserver.data.SpawnTable;
import net.sf.l2j.gameserver.data.manager.BuyListManager;
import net.sf.l2j.gameserver.data.sql.BookmarkTable;
import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.data.sql.ServerMemoTable;
import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.data.xml.AnnouncementData;
import net.sf.l2j.gameserver.data.xml.ArmorSetData;
import net.sf.l2j.gameserver.data.xml.AugmentationData;
import net.sf.l2j.gameserver.data.xml.FishData;
import net.sf.l2j.gameserver.data.xml.HerbDropData;
import net.sf.l2j.gameserver.data.xml.MultisellData;
import net.sf.l2j.gameserver.data.xml.NewbieBuffData;
import net.sf.l2j.gameserver.data.xml.SoulCrystalData;
import net.sf.l2j.gameserver.data.xml.SpellbookData;
import net.sf.l2j.gameserver.data.xml.StaticObjectData;
import net.sf.l2j.gameserver.data.xml.SummonItemData;
import net.sf.l2j.gameserver.data.xml.TeleportLocationData;
import net.sf.l2j.gameserver.data.xml.WalkerRouteData;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.*;
import net.sf.l2j.gameserver.instancemanager.games.MonsterRace;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.olympiad.OlympiadGameManager;
import net.sf.l2j.gameserver.model.partymatching.PartyMatchRoomList;
import net.sf.l2j.gameserver.model.partymatching.PartyMatchWaitingList;
import net.sf.l2j.gameserver.model.vehicles.BoatGiranTalking;
import net.sf.l2j.gameserver.model.vehicles.BoatGludinRune;
import net.sf.l2j.gameserver.model.vehicles.BoatInnadrilTour;
import net.sf.l2j.gameserver.model.vehicles.BoatRunePrimeval;
import net.sf.l2j.gameserver.model.vehicles.BoatTalkingGludin;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.L2GamePacketHandler;
import net.sf.l2j.gameserver.scripting.ScriptManager;
import net.sf.l2j.gameserver.taskmanager.AttackStanceTaskManager;
import net.sf.l2j.gameserver.taskmanager.DecayTaskManager;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;
import net.sf.l2j.gameserver.taskmanager.ItemsOnGroundTaskManager;
import net.sf.l2j.gameserver.taskmanager.MovementTaskManager;
import net.sf.l2j.gameserver.taskmanager.PvpFlagTaskManager;
import net.sf.l2j.gameserver.taskmanager.RandomAnimationTaskManager;
import net.sf.l2j.gameserver.taskmanager.ShadowItemTaskManager;
import net.sf.l2j.gameserver.taskmanager.WaterTaskManager;
import net.sf.l2j.gameserver.xmlfactory.XMLDocumentFactory;
import net.sf.l2j.util.DeadLockDetector;
import net.sf.l2j.util.IPv4Filter;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

@Slf4j
public class GameServer {

	private static GameServer instance;

	/**
	 * Global event bus
	 */
	@Getter private static EventBus eventBus = new EventBus();
	@Getter private static Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()));
	private SelectorThread<L2GameClient> selectorThread;

	public static void main(String[] args) throws Exception {
		instance = new GameServer();

		instance.load();
	}

	public GameServer() {
	}

	private void load() {
		// Create log folder
		new File("./log").mkdir();
		new File("./log/chat").mkdir();
		new File("./log/console").mkdir();
		new File("./log/error").mkdir();
		new File("./log/gmaudit").mkdir();
		new File("./log/item").mkdir();
		new File("./data/crests").mkdirs();

		StringUtil.printSection("aCis");

		// Initialize config
		Config.loadGameServer();

		// Factories
		XMLDocumentFactory.getInstance();
		L2DatabaseFactory.getInstance();

		StringUtil.printSection("ThreadPool");
		ThreadPool.init();

		StringUtil.printSection("IdFactory");
		IdFactory.getInstance();

		StringUtil.printSection("World");
		World.getInstance();
		MapRegionTable.getInstance();
		AnnouncementData.getInstance();
		ServerMemoTable.getInstance();

		StringUtil.printSection("Skills");
		SkillTable.getInstance();
		SkillTreeTable.getInstance();

		StringUtil.printSection("Items");
		ItemTable.getInstance();
		SummonItemData.getInstance();
		DyeTable.getInstance();
		BuyListManager.getInstance();
		MultisellData.getInstance();
		ArmorSetData.getInstance();
		FishData.getInstance();
		SpellbookData.getInstance();
		SoulCrystalData.getInstance();
		AugmentationData.getInstance();
		CursedWeaponsManager.getInstance();

		StringUtil.printSection("Admins");
		AdminData.getInstance();
		BookmarkTable.getInstance();
		MovieMakerManager.getInstance();
		PetitionManager.getInstance();

		StringUtil.printSection("Characters");
		CharTemplateTable.getInstance();
		PlayerNameTable.getInstance();
		NewbieBuffData.getInstance();
		TeleportLocationData.getInstance();
		HtmCache.getInstance();
		PartyMatchWaitingList.getInstance();
		PartyMatchRoomList.getInstance();
		RaidBossPointsManager.getInstance();

		StringUtil.printSection("Community server");
		if (Config.ENABLE_COMMUNITY_BOARD) {
			ForumsBBSManager.getInstance().initRoot(); // Forums has to be loaded before clan data
		} else {
			log.info("Community server is disabled.");
		}

		StringUtil.printSection("Clans");
		CrestCache.getInstance();
		ClanTable.getInstance();
		AuctionManager.getInstance();
		ClanHallManager.getInstance();

		StringUtil.printSection("Geodata & Pathfinding");
		GeoEngine.getInstance();

		StringUtil.printSection("Zones");
		ZoneManager.getInstance();

		StringUtil.printSection("Task Managers");
		AttackStanceTaskManager.getInstance();
		DecayTaskManager.getInstance();
		GameTimeTaskManager.getInstance();
		ItemsOnGroundTaskManager.getInstance();
		MovementTaskManager.getInstance();
		PvpFlagTaskManager.getInstance();
		RandomAnimationTaskManager.getInstance();
		ShadowItemTaskManager.getInstance();
		WaterTaskManager.getInstance();

		StringUtil.printSection("Castles");
		CastleManager.getInstance();

		StringUtil.printSection("Seven Signs");
		SevenSigns.getInstance().spawnSevenSignsNPC();
		SevenSignsFestival.getInstance();

		StringUtil.printSection("Manor Manager");
		CastleManorManager.getInstance();

		StringUtil.printSection("NPCs");
		BufferTable.getInstance();
		HerbDropData.getInstance();
		NpcTable.getInstance();
		WalkerRouteData.getInstance();
		DoorTable.getInstance().spawn();
		StaticObjectData.getInstance();
		SpawnTable.getInstance();
		RaidBossSpawnManager.getInstance();
		GrandBossManager.getInstance();
		DayNightSpawnManager.getInstance();
		DimensionalRiftManager.getInstance();

		StringUtil.printSection("Olympiads & Heroes");
		OlympiadGameManager.getInstance();
		Olympiad.getInstance();
		Hero.getInstance();

		StringUtil.printSection("Four Sepulchers");
		FourSepulchersManager.getInstance().init();

		StringUtil.printSection("Quests & Scripts");
		QuestDataTable.getInstance();
		ScriptManager.getInstance();

		StringUtil.printSection("FinEx Loader");
		FinexLoader.getInstance();

		if (Config.ALLOW_BOAT) {
			BoatManager.getInstance();
			BoatGiranTalking.load();
			BoatGludinRune.load();
			BoatInnadrilTour.load();
			BoatRunePrimeval.load();
			BoatTalkingGludin.load();
		}

		StringUtil.printSection("Events");
		MonsterRace.getInstance();

		if (Config.ALLOW_WEDDING) {
			CoupleManager.getInstance();
		}

		if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED) {
			FishingChampionshipManager.getInstance();
		}

		StringUtil.printSection("Handlers");
		log.info("Handlers: Loaded {} handlers.", HandlerTable.getInstance().getHolder().size());
		log.info("AutoSpawnHandler: Loaded " + AutoSpawnManager.getInstance().size() + " handlers.");
		log.info("AdminCommandHandler: Loaded " + AdminCommandHandler.getInstance().size() + " handlers.");

		StringUtil.printSection("System");
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		ForumsBBSManager.getInstance();
		log.info("IdFactory: Free ObjectIDs remaining: " + IdFactory.getInstance().size());

		if (Config.DEADLOCK_DETECTOR) {
			log.info("Deadlock detector is enabled. Timer: " + Config.DEADLOCK_CHECK_INTERVAL + "s.");

			final DeadLockDetector deadDetectThread = new DeadLockDetector();
			deadDetectThread.setDaemon(true);
			deadDetectThread.start();
		} else {
			log.info("Deadlock detector is disabled.");
		}

		System.gc();

		log.info("Gameserver have started, used memory: " + SysUtil.getUsedMemory() + " / " + SysUtil.getMaxMemory() + " Mo.");
		log.info("Maximum allowed players: " + Config.MAXIMUM_ONLINE_USERS);

		StringUtil.printSection("Login");
		LoginServerThread.getInstance().start();

		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = Config.MMO_MAX_READ_PER_PASS;
		sc.MAX_SEND_PER_PASS = Config.MMO_MAX_SEND_PER_PASS;
		sc.SLEEP_TIME = Config.MMO_SELECTOR_SLEEP_TIME;
		sc.HELPER_BUFFER_COUNT = Config.MMO_HELPER_BUFFER_COUNT;

		final L2GamePacketHandler handler = new L2GamePacketHandler();
		try {
			selectorThread = new SelectorThread<>(sc, handler, handler, handler, new IPv4Filter());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}

		InetAddress bindAddress = null;
		if (!Config.GAMESERVER_HOSTNAME.equals("*")) {
			try {
				bindAddress = InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
			} catch (UnknownHostException e1) {
				log.error("WARNING: The GameServer bind address is invalid, using all available IPs. Reason: " + e1.getMessage(), e1);
			}
		}

		try {
			selectorThread.openServerSocket(bindAddress, Config.PORT_GAME);
		} catch (IOException e) {
			log.error("FATAL: Failed to open server socket. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		selectorThread.start();
	}

	public static GameServer getInstance() {
		return instance;
	}

	public SelectorThread<L2GameClient> getSelectorThread() {
		return selectorThread;
	}
}
