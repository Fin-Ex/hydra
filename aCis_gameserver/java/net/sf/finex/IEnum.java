/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex;

/**
 *
 * @author finfan
 */
public interface IEnum {
	public int getId();
	public String getEnumName();
	public String getNormalName();
	public int getMask();
}
