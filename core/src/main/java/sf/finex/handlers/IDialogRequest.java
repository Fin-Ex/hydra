/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.handlers;

import sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
public interface IDialogRequest {

	public <T> T handle(Player player, Object... args);
}
