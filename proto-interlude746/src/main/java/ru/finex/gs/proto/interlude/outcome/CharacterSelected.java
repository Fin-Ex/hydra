package ru.finex.gs.proto.interlude.outcome;

import lombok.Data;
import ru.finex.gs.model.dto.SelectedAvatarDto;
import ru.finex.gs.model.entity.ClanEntity;
import ru.finex.gs.model.entity.PlayerEntity;
import ru.finex.gs.model.entity.PositionEntity;
import ru.finex.gs.model.entity.StatusEntity;
import ru.finex.gs.proto.network.L2GameServerPacket;
import ru.finex.gs.proto.network.Opcode;
import ru.finex.gs.proto.network.OutcomePacket;
import sf.l2j.gameserver.model.base.ClassId;

/**
 * @author m0nster.mind
 */
@Data
@OutcomePacket(@Opcode(0x15))
public class CharacterSelected extends L2GameServerPacket {

	private int sessionId;
	private int runtimeId;
	private SelectedAvatarDto avatar;

	@Override
	protected final void writeImpl() {
		writeC(0x15);

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
		writeD(player.getSex().ordinal());
		writeD(player.getRace().ordinal());
		writeD(player.getAppearanceClass().getNetworkId(player.getRace()));
		writeD(0x01); // selected
		writeD((int)position.getX());
		writeD((int)position.getY());
		writeD((int)position.getZ());
		writeF(status.getHp());
		writeF(status.getMp());
		writeD(0); // sp
		writeQ(0); // exp
		writeD(1); // level
		writeD(0); // karma
		writeD(0); // pk count
		writeD(1); // int
		writeD(1); // str
		writeD(1); // con
		writeD(1); // men
		writeD(1); // dex
		writeD(1); // wit

		for (int i = 0; i < 30; i++) {
			writeD(0x00);
		}

		writeD(0x00);
		writeD(0x00);

		writeD(0); // game time

		writeD(0x00);

		writeD(ClassId.HumanFighter.ordinal());

		writeD(0x00); // GG
		writeD(0x00); // GG
		writeD(0x00); // GG
		writeD(0x00); // GG
	}
}
