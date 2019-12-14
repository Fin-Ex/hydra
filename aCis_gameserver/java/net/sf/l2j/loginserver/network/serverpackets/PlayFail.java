package net.sf.l2j.loginserver.network.serverpackets;

import org.slf4j.LoggerFactory;

public final class PlayFail extends L2LoginServerPacket {

	public static enum PlayFailReason {
		REASON_SYSTEM_ERROR(0x01),
		REASON_USER_OR_PASS_WRONG(0x02),
		REASON3(0x03),
		REASON4(0x04),
		REASON_TOO_MANY_PLAYERS(0x0f);

		private final int _code;

		PlayFailReason(int code) {
			_code = code;
		}

		public final int getCode() {
			return _code;
		}
	}

	private final PlayFailReason _reason;

	public PlayFail(PlayFailReason reason) {
		_reason = reason;
	}

	@Override
	protected void write() {
		writeC(0x06);
		writeC(_reason.getCode());
	}
}
