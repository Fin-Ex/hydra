/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.actor.ai;

import lombok.extern.slf4j.Slf4j;
import sf.l2j.gameserver.model.actor.Creature;

@Slf4j
public class NotifyAITask implements Runnable {

	private final Creature activeChar;
	private final CtrlEvent event;

	public NotifyAITask(Creature activeChar, CtrlEvent event) {
		this.activeChar = activeChar;
		this.event = event;
	}

	@Override
	public void run() {
		try {
			activeChar.getAI().notifyEvent(event, null);
		} catch (Throwable t) {
			log.error("", t);
		}
	}

}
