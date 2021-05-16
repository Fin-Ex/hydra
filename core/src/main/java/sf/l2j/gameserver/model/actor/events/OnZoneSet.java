/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.actor.events;

import lombok.Data;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.zone.ZoneId;

/**
 *
 * @author finfan
 */
@Data
public class OnZoneSet {

	private final Creature creature;
	private final ZoneId zoneId;
	private final boolean entering;
}