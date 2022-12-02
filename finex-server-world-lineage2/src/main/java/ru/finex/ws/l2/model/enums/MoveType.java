package ru.finex.ws.l2.model.enums;

public enum MoveType {
	Walk(0),
	Run(1);

	private final int id;

	MoveType(int id) {
		this.id = id;
	}

	public static int findBy(boolean isRunning) {
		return isRunning ? Run.id : Walk.id;
	}
}
