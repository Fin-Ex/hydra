/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.actor.events;

import lombok.Data;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;

/**
 *
 * @author finfan
 */
@Data
public class OnZoneCheck {

	private final Player player;
	private final L2ZoneType zone;
	private final boolean enter;
}
