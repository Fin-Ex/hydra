package ru.finex.gs.proto.interlude.outcome;

import lombok.Data;
import ru.finex.gs.model.AuthFailReason;
import ru.finex.gs.proto.network.L2GameServerPacket;
import ru.finex.gs.proto.network.Opcode;
import ru.finex.gs.proto.network.OutcomePacket;

/**
 * @author m0nster.mind
 */
@Data
@OutcomePacket(@Opcode(0x14))
public class AuthLoginFail extends L2GameServerPacket {

	private AuthFailReason reason;

	@Override
	protected final void writeImpl() {
		writeC(0x14);
		writeD(reason.ordinal());
	}
}
