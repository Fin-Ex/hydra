/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.enums;

import lombok.Getter;

/**
 *
 * @author finfan
 */
public enum ESocialAction {
	Nod(2),
	Victory(3),
	Attack(4),
	No(5),
	Yes(6),
	Bow(7),
	Unaware(8),
	Waiting(9),
	Laugh(10),
	Clap(11),
	Dance(12),
	Sad(13),
	LevelUp(15),
	Pickup(16),
	FakeDeath(17),
	StandUp(18);

	@Getter private final int id;

	private ESocialAction(int id) {
		this.id = id;
	}
}
