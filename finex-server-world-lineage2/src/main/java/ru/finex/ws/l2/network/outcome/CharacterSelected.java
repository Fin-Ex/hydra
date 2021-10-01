package ru.finex.ws.l2.network.outcome;

import lombok.Data;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.model.dto.SelectedAvatarDto;
import ru.finex.ws.l2.model.entity.ClanEntity;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.ws.l2.model.entity.PositionEntity;
import ru.finex.ws.l2.model.entity.StatusEntity;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.OutcomePacket;
import ru.finex.ws.l2.network.model.L2GameServerPacket;

/**
 * @author m0nster.mind
 */
@Data
@OutcomePacket(@Opcode(0x0b))
public class CharacterSelected extends L2GameServerPacket {

	private int sessionId;
	private int runtimeId;
	private SelectedAvatarDto avatar;

	@Override
	protected final void writeImpl() {
		writeC(0x0b);

		PlayerEntity player = avatar.getPlayer();
		ClanEntity clan = avatar.getClan();
		PositionEntity position = avatar.getPosition();
		StatusEntity status = avatar.getStatus();

		writeS(player.getName());
		writeD(runtimeId);
		writeS(player.getTitle());
		writeD(sessionId);
		writeD(clan.getPersistenceId());
		writeD(0x00); // FIXME m0nster.mind: access level
		writeD(player.getGender().ordinal());
		writeD(player.getRace().ordinal());
		writeD(player.getAppearanceClass().getNetworkId(player.getRace()));
		writeD(0x01); // selected
		writeD((int)position.getX());
		writeD((int)position.getY());
		writeD((int)position.getZ());
		writeF(status.getHp());
		writeF(status.getMp());
		writeQ(0); // sp
		writeQ(0); // exp
		writeD(1); // level
		writeD(0); // karma
		writeD(0); // pk count
		writeD(0); // game time
		writeD(0x00);
		writeD(ClassId.HumanFighter.ordinal());
		writeB(new byte[16]);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeB(new byte[28]);
		writeD(0x00);
	}
}
