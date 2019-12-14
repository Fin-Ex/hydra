/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.data;

import lombok.Data;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author finfan
 */
@Data
public class RankerData {

	private final int objectId;
	private int classId;
	private int raceId;
	private int sexId;
	private int points;

	public RankerData(int objectId) {
		this.objectId = objectId;
	}

	public RankerData(Player player) {
		this.objectId = player.getObjectId();
		this.classId = player.getClassId().getId();
		this.raceId = player.getRace().ordinal();
		this.sexId = player.getAppearance().getSex().ordinal();
	}
}
