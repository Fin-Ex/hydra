/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler;

/**
 *
 * @author finfan
 */
public interface IHandler {
	public void invoke(Object...args);
	public default <T> T[] commands() {
		return null;
	}
}
