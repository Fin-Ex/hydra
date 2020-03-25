/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.interfaces;

/**
 *
 * @author FinFan
 */
public interface IPersistence {

	public void store();

	public void restore();

	public void delete();

	public void remove(Object... args);
}
