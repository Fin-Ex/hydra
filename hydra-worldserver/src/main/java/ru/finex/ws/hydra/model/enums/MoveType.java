package ru.finex.ws.hydra.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MoveType implements IdEnum {
	WALK(0),
	RUN(1);

	private final int id;

	public static int findBy(boolean isRunning) {
		return isRunning ? RUN.id : WALK.id;
	}
}
