/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.actor.events;

import lombok.Data;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;

/**
 *
 * @author finfan
 */
@Data
public class OnAddItem {
	private final String from;
	private final Player owner;
	private final ItemInstance item;
}
