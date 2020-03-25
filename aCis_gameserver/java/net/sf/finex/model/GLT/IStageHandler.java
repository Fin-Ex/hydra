/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import net.sf.finex.events.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author finfan
 */
public interface IStageHandler {

	Logger log = LoggerFactory.getLogger(IStageHandler.class);

	public void call();

	public void clear();
	
	public default EventBus getListener() {
		return null;
	}
}
