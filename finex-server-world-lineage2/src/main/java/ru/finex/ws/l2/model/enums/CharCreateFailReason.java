package ru.finex.ws.l2.model.enums;

public enum CharCreateFailReason {
	REASON_CREATION_FAILED,
	REASON_TOO_MANY_CHARACTERS,
	REASON_NAME_ALREADY_EXISTS,
	REASON_16_ENG_CHARS,
	REASON_INCORRECT_NAME,
	REASON_CREATE_NOT_ALLOWED,
	REASON_CHOOSE_ANOTHER_SERVER;

	public int getId() {
		return ordinal();
	}
}
