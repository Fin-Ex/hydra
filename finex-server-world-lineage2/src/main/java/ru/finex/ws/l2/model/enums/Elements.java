package ru.finex.ws.l2.model.enums;

import lombok.Getter;

@Getter
public enum Elements {
	NONE(-2),
	FIRE(0),
	WATER(1),
	WIND(2),
	EARTH(3),
	HOLY(4),
	DARK(5);

	private final int id;

	Elements(int id) {
		this.id = id;
	}
}
