/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.enums;

import lombok.Getter;

/**
 *
 * @author FinFan
 */
public enum ESkillOperateType {
	OP_PASSIVE("Passive"),
	OP_ACTIVE("Active"),
	OP_TOGGLE("Toggle (On/Off)");
	
	@Getter private final String name;

	private ESkillOperateType(String name) {
		this.name = name;
	}
}
