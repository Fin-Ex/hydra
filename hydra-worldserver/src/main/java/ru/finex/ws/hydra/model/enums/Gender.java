package ru.finex.ws.hydra.model.enums;

/**
 * @author m0nster.mind
 */
public enum Gender implements IdEnum {
	MALE,
	FEMALE,
	ETC;

	@Override
	public int getId() {
		return ordinal();
	}

	public static Gender ofId(int id) {
		return Gender.values()[id];
	}

}
