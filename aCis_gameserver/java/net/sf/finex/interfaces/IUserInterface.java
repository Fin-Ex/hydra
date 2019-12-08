/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.interfaces;

import org.slf4j.LoggerFactory;

import net.sf.finex.enums.EUIEventType;

/**
 *
 * @author FinFan
 */
public interface IUserInterface {
	public void showHtml(EUIEventType event);
}
