/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author finfan
 */
public interface IHandler {
	static final Logger log = LoggerFactory.getLogger(IHandler.class);
	public void invoke(Object...args);
	public default <T> T[] commands() {
		return null;
	}
}
