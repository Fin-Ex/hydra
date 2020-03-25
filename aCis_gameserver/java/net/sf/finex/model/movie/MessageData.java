/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie;

import lombok.Getter;
import lombok.Setter;
import net.sf.l2j.gameserver.network.clientpackets.Say2;

/**
 *
 * @author finfan
 */
@Getter
@Setter
public class MessageData {

	private final String message;
	private final int say2;

	public MessageData(String message, int say2) {
		this.message = message;
		this.say2 = say2;
	}

	public MessageData(String msg) {
		this.message = msg;
		this.say2 = Say2.ALL;
	}
}
