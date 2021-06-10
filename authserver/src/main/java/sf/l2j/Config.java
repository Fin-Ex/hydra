package sf.l2j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sf.l2j.commons.config.ExProperties;

import java.io.File;
import java.io.IOException;

/**
 * This class contains global server configuration.<br>
 * It has static final fields initialized from configuration files.<br>
 *
 * @author mkizub
 */
public final class Config {

	private static final Logger _log = LoggerFactory.getLogger(Config.class.getName());

	public static final String LOGIN_CONFIGURATION_FILE = "./config/loginserver.properties";

	// --------------------------------------------------
	// Loginserver
	// --------------------------------------------------
	public static String HOSTNAME;

	public static String LOGIN_BIND_ADDRESS;
	public static int PORT_LOGIN;

	public static int GAME_SERVER_LOGIN_PORT;
	public static String GAME_SERVER_LOGIN_HOST;

	public static int LOGIN_TRY_BEFORE_BAN;
	public static int LOGIN_BLOCK_AFTER_BAN;
	public static boolean ACCEPT_NEW_GAMESERVER;

	public static boolean SHOW_LICENCE;

	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static int DATABASE_MAX_CONNECTIONS;

	public static boolean AUTO_CREATE_ACCOUNTS;

	public static boolean LOG_LOGIN_CONTROLLER;

	public static boolean FLOOD_PROTECTION;
	public static int FAST_CONNECTION_LIMIT;
	public static int NORMAL_CONNECTION_TIME;
	public static int FAST_CONNECTION_TIME;
	public static int MAX_CONNECTION_PER_IP;

	// --------------------------------------------------
	// Those "hidden" settings haven't configs to avoid admins to fuck their server
	// You still can experiment changing values here. But don't say I didn't warn you.
	// --------------------------------------------------
	/**
	 * Reserve Host on LoginServerThread
	 */
	public static boolean RESERVE_HOST_ON_LOGIN = false; // default false

	/**
	 * MMO settings
	 */
	public static int MMO_SELECTOR_SLEEP_TIME = 20; // default 20
	public static int MMO_MAX_SEND_PER_PASS = 80; // default 80
	public static int MMO_MAX_READ_PER_PASS = 80; // default 80
	public static int MMO_HELPER_BUFFER_COUNT = 20; // default 20

	// --------------------------------------------------
	/**
	 * Initialize {@link ExProperties} from specified configuration file.
	 *
	 * @param filename : File name to be loaded.
	 * @return ExProperties : Initialized {@link ExProperties}.
	 */
	public static final ExProperties initProperties(String filename) {
		final ExProperties result = new ExProperties();

		try {
			result.load(new File(filename));
		} catch (IOException e) {
			_log.warn("Config: Error loading \"" + filename + "\" config.");
		}

		return result;
	}

	/**
	 * Loads loginserver settings.<br>
	 * IP addresses, database, account, misc.
	 */
	private static final void loadLogin() {
		final ExProperties server = initProperties(LOGIN_CONFIGURATION_FILE);
		HOSTNAME = server.getProperty("Hostname", "localhost");

		LOGIN_BIND_ADDRESS = server.getProperty("LoginserverHostname", "*");
		PORT_LOGIN = server.getProperty("LoginserverPort", 2106);

		GAME_SERVER_LOGIN_HOST = server.getProperty("LoginHostname", "*");
		GAME_SERVER_LOGIN_PORT = server.getProperty("LoginPort", 9014);

		LOGIN_TRY_BEFORE_BAN = server.getProperty("LoginTryBeforeBan", 3);
		LOGIN_BLOCK_AFTER_BAN = server.getProperty("LoginBlockAfterBan", 600);
		ACCEPT_NEW_GAMESERVER = server.getProperty("AcceptNewGameServer", false);

		SHOW_LICENCE = server.getProperty("ShowLicence", true);

		DATABASE_URL = server.getProperty("URL", "jdbc:mysql://localhost/acis");
		DATABASE_LOGIN = server.getProperty("Login", "root");
		DATABASE_PASSWORD = server.getProperty("Password", "");
		DATABASE_MAX_CONNECTIONS = server.getProperty("MaximumDbConnections", 10);

		AUTO_CREATE_ACCOUNTS = server.getProperty("AutoCreateAccounts", true);

		LOG_LOGIN_CONTROLLER = server.getProperty("LogLoginController", false);

		FLOOD_PROTECTION = server.getProperty("EnableFloodProtection", true);
		FAST_CONNECTION_LIMIT = server.getProperty("FastConnectionLimit", 15);
		NORMAL_CONNECTION_TIME = server.getProperty("NormalConnectionTime", 700);
		FAST_CONNECTION_TIME = server.getProperty("FastConnectionTime", 350);
		MAX_CONNECTION_PER_IP = server.getProperty("MaxConnectionPerIP", 50);
	}

	public static final void loadLoginServer() {
		_log.info("Loading loginserver configuration files.");

		// login settings
		loadLogin();
	}
}
