/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.enums;

import lombok.Getter;
import net.sf.l2j.gameserver.network.SystemMessageId;

/**
 *
 * @author FinFan
 */
public enum EPartyLoot {
	ITEM_LOOTER(SystemMessageId.LOOTING_FINDERS_KEEPERS),
	ITEM_RANDOM(SystemMessageId.LOOTING_RANDOM),
	ITEM_RANDOM_SPOIL(SystemMessageId.LOOTING_RANDOM_INCLUDE_SPOIL),
	ITEM_ORDER(SystemMessageId.LOOTING_BY_TURN),
	ITEM_ORDER_SPOIL(SystemMessageId.LOOTING_BY_TURN_INCLUDE_SPOIL);

	@Getter
	private final SystemMessageId messageId;

	private EPartyLoot(SystemMessageId messageId) {
		this.messageId = messageId;
	}

	public static final EPartyLoot[] VALUES = values();
}
