package net.sf.l2j.gameserver.handler;

import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import net.sf.l2j.gameserver.handler.usercommandhandlers.Challenge;
import net.sf.l2j.gameserver.handler.usercommandhandlers.ChannelDelete;
import net.sf.l2j.gameserver.handler.usercommandhandlers.ChannelLeave;
import net.sf.l2j.gameserver.handler.usercommandhandlers.ChannelListUpdate;
import net.sf.l2j.gameserver.handler.usercommandhandlers.ClanPenalty;
import net.sf.l2j.gameserver.handler.usercommandhandlers.ClanWarsList;
import net.sf.l2j.gameserver.handler.usercommandhandlers.DisMount;
import net.sf.l2j.gameserver.handler.usercommandhandlers.Escape;
import net.sf.l2j.gameserver.handler.usercommandhandlers.Loc;
import net.sf.l2j.gameserver.handler.usercommandhandlers.Mount;
import net.sf.l2j.gameserver.handler.usercommandhandlers.OlympiadStat;
import net.sf.l2j.gameserver.handler.usercommandhandlers.PartyInfo;
import net.sf.l2j.gameserver.handler.usercommandhandlers.SiegeStatus;
import net.sf.l2j.gameserver.handler.usercommandhandlers.Time;
import net.sf.l2j.gameserver.handler.usercommandhandlers.WarsmithInfo;

public class UserCommandHandler {

	private final Map<Integer, IUserCommandHandler> _datatable = new HashMap<>();

	public static UserCommandHandler getInstance() {
		return SingletonHolder._instance;
	}

	protected UserCommandHandler() {
		registerUserCommandHandler(new ChannelDelete());
		registerUserCommandHandler(new ChannelLeave());
		registerUserCommandHandler(new ChannelListUpdate());
		registerUserCommandHandler(new ClanPenalty());
		registerUserCommandHandler(new ClanWarsList());
		registerUserCommandHandler(new DisMount());
		registerUserCommandHandler(new Escape());
		registerUserCommandHandler(new Loc());
		registerUserCommandHandler(new Mount());
		registerUserCommandHandler(new OlympiadStat());
		registerUserCommandHandler(new PartyInfo());
		registerUserCommandHandler(new SiegeStatus());
		registerUserCommandHandler(new Time());
		registerUserCommandHandler(new WarsmithInfo());
		registerUserCommandHandler(new Challenge());
	}

	public void registerUserCommandHandler(IUserCommandHandler handler) {
		for (int id : handler.getUserCommandList()) {
			_datatable.put(id, handler);
		}
	}

	public IUserCommandHandler getUserCommandHandler(int userCommand) {
		return _datatable.get(userCommand);
	}

	public int size() {
		return _datatable.size();
	}

	private static class SingletonHolder {

		protected static final UserCommandHandler _instance = new UserCommandHandler();
	}
}
