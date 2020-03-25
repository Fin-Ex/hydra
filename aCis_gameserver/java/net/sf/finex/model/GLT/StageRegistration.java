/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import java.time.LocalTime;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.network.SystemMessageId;

/**
 *
 * @author finfan
 */
public class StageRegistration implements IStageHandler {

	@Override
	public void call() {
		World.getInstance().broadcastSystemMessagePacket(SystemMessageId.THE_GRAND_LETHAL_TOURNAMENT_REGISTRATION_IS_OPENED);
		GLTController.getInstance().startNextTimer(LocalTime.now().plusMinutes(1).getMinute() + " " + LocalTime.now().getHour() + " * * *");
	}

	@Override
	public void clear() {
	}
}
