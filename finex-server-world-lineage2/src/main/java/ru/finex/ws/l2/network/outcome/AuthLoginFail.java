package ru.finex.ws.l2.network.outcome;

import lombok.Data;
import ru.finex.ws.l2.auth.model.AuthFailReason;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.OutcomePacket;
import ru.finex.ws.l2.network.model.L2GameServerPacket;

/**
 * @author m0nster.mind
 */
@Data
@OutcomePacket(@Opcode(0x0A))
public class AuthLoginFail extends L2GameServerPacket {

	private AuthFailReason reason;

	@Override
	protected final void writeImpl() {
		writeC(0x0A);
		writeD(reason.isSuccess());
		writeD(reason.ordinal());
	}
}
