/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.finex.ws.l2.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author FinFan
 */
@RequiredArgsConstructor
public enum StoreType {
	NONE(0),
	SELL(1),
	SELL_MANAGE(2),
	BUY(3),
	BUY_MANAGE(4),
	MANUFACTURE(5),
	PACKAGE_SELL(8);

	@Getter private final int id;

}
