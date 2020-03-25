/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie;

import lombok.Data;
import net.sf.finex.model.movie.actions.AbstractActorAction;

/**
 *
 * @author finfan
 */
@Data
public class ActionData {

	private final AbstractActorAction action;
	private int id;

	public ActionData(AbstractActorAction action) {
		this.action = action;
	}
	
	private static int hunger = 100;
	private static boolean dead = false;
	public static void main(String... args) {
		test(true, false);
	}
	
	public static final void test(Object...args) {
		boolean f1 = (boolean) args[0];
		boolean f2 = (boolean) args[1];
		boolean f3 = (boolean) args[2];
	}
}
