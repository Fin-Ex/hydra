package net.sf.l2j.gameserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public class CharCreateFail extends L2GameServerPacket {

	public static final int REASON_CREATION_FAILED = 0x00; // "Your character creation has failed."
	public static final int REASON_TOO_MANY_CHARACTERS = 0x01; // "You cannot create another character. Please delete the existing character and try again."
	public static final int REASON_NAME_ALREADY_EXISTS = 0x02; // "This name already exists."
	public static final int REASON_16_ENG_CHARS = 0x03; // "Your title cannot exceed 16 characters in length. Please try again."
	public static final int REASON_INCORRECT_NAME = 0x04; // "Incorrect name. Please try again."
	public static final int REASON_CREATE_NOT_ALLOWED = 0x05; // "Characters cannot be created from this server."
	public static final int REASON_CHOOSE_ANOTHER_SVR = 0x06; // "Unable to create character. You are unable to create a new character on the selected server. A restriction is in place which restricts users from creating characters on different servers where no previous character exists. Please
	// choose another server."

	private final int _error;

	public CharCreateFail(int errorCode) {
		_error = errorCode;
	}

	@Override
	protected final void writeImpl() {
		writeC(0x1a);
		writeD(_error);
	}
}
