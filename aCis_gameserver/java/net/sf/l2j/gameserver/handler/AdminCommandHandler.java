package net.sf.l2j.gameserver.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.GameServer;

@Slf4j
public class AdminCommandHandler {

	private final Map<Integer, IAdminCommandHandler> _datatable = new HashMap<>();

	public static AdminCommandHandler getInstance() {
		return SingletonHolder._instance;
	}

	protected AdminCommandHandler() {
		try {
			for (Class<?> clazz : GameServer.getReflections().getSubTypesOf(IAdminCommandHandler.class)) {
				registerAdminCommandHandler((IAdminCommandHandler) clazz.getConstructor().newInstance());
			}
		} catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
			log.info("Error in IItemHandler loading.", e);
		}
	}

	private void registerAdminCommandHandler(IAdminCommandHandler handler) {
		for (String id : handler.getAdminCommandList()) {
			_datatable.put(id.hashCode(), handler);
		}
	}

	public IAdminCommandHandler getAdminCommandHandler(String adminCommand) {
		String command = adminCommand;

		if (adminCommand.contains(" ")) {
			command = adminCommand.substring(0, adminCommand.indexOf(" "));
		}

		return _datatable.get(command.hashCode());
	}

	public int size() {
		return _datatable.size();
	}

	private static class SingletonHolder {

		protected static final AdminCommandHandler _instance = new AdminCommandHandler();
	}
}
