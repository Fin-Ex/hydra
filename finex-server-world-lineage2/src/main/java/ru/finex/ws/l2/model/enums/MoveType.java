package ru.finex.ws.l2.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MoveType {
	Walk(0),
	Run(1);

	@Getter
	private final int id;

	public static int findBy(boolean isRunning) {
		return isRunning ? Run.id : Walk.id;
	}
}
