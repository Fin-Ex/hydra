/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 *
 * @author finfan
 */
@Slf4j
public final class HandlerTable {

	@Getter private static final HandlerTable instance = new HandlerTable();

	@Getter private final Map<Class<?>, IHandler> holder = new HashMap<>();

	private HandlerTable() {
		try {
			for (Class<?> clazz : GameServer.getReflections().getSubTypesOf(IHandler.class)) {
				holder.put(clazz, (IHandler) clazz.getConstructor().newInstance());
			}
		} catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
			log.error("Error in handler loading.", e);
		}
	}

	public IHandler get(int cmdid) {
		for(IHandler next : holder.values()) {
			if (next.commands() != null && next.commands() instanceof Integer[]) {
				final Integer[] cmds = next.commands();
				for(Integer nextInt : cmds) {
					if(nextInt == cmdid) {
						return next;
					}
				}
			}
		}
		return null;
	}

	public IHandler get(EAdminCommandType cmd) {
		for(IHandler next : holder.values()) {
			if (next.commands() != null && next.commands() instanceof EAdminCommandType[]) {
				final EAdminCommandType[] cmds = next.commands();
				for(EAdminCommandType nextCmd : cmds) {
					if(nextCmd == cmd) {
						return next;
					}
				}
			}
		}
		return null;
	}

	public IHandler get(String handlerClassName) {
		for(IHandler next : holder.values()) {
			if (next.getClass().getSimpleName().equalsIgnoreCase(handlerClassName)) {
				return next;
			}
		}
		return null;
	}

	public IHandler get(Class<?> type) {
		if(!holder.containsKey(type)) {
			log.warn("Handler {} doesnt exist.", type);
			return null;
		}
		
		for(IHandler next : holder.values()) {
			if (next.getClass() == type) {
				return next;
			}
		}
		return null;
	}

	public IHandler get(ESkillType skillType) {
		for(IHandler next : holder.values()) {
			if(next.commands() != null && next.commands() instanceof ESkillType[]) {
				final ESkillType[] types = next.commands();
				for(ESkillType t : types) {
					if(t == skillType) {
						return next;
					}
				}
			}
		}
		return null;
	}
}
