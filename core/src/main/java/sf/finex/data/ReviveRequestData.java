/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.data;

import lombok.Data;
import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.actor.Player;

/**
 *
 * @author FinFan
 */
@Data
public class ReviveRequestData {

	private final Playable reanimator;
	private final Player target;
	private double revivePower;
	private boolean revivePet;
}
