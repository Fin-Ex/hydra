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
public enum EStoreType {
	NONE(0),
	SELL(1),
	SELL_MANAGE(2),
	BUY(3),
	BUY_MANAGE(4),
	MANUFACTURE(5),
	PACKAGE_SELL(8);
	
	@Getter private int id;

	private EStoreType(int id) {
		this.id = id;
	}
}
