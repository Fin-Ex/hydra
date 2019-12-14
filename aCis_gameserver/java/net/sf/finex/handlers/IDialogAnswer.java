/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
public interface IDialogAnswer {

	public void handle(Player activeChar, int answer, int requesterId);
}
