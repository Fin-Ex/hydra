package ru.finex.ws.hydra.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Elements implements IdEnum {
	NONE(-2),
	FIRE(0),
	WATER(1),
	WIND(2),
	EARTH(3),
	HOLY(4),
	DARK(5);

	private final int id;

}
