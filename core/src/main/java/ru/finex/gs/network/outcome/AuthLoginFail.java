package ru.finex.gs.network.outcome;

import lombok.Data;
import ru.finex.gs.model.AuthFailReason;
import ru.finex.gs.network.Opcode;
import ru.finex.gs.network.OutcomePacket;
import sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

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
