/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.enums;

import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 *
 * @author FinFan
 */
public enum EPunishLevel {
	NONE(""),
	CHAT("chat banned"),
	JAIL("jailed"),
	CHAR("banned"),
	ACC("banned");
	
	@Getter private final String punishString;

	private EPunishLevel(String string) {
		punishString = string;
	}
}
