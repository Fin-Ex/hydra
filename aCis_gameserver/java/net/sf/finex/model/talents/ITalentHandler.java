/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.talents;

/**
 *
 * @author finfan
 */
public interface ITalentHandler {

	public <T> T invoke(Object... args);
}
