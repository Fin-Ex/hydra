package ru.finex.ws.l2.model;

/**
 * @author m0nster.mind
 */
public enum Gender {
	MALE,
	FEMALE,
	ETC;

	public static Gender ofId(int id) {
		return Gender.values()[id];
	}
}
