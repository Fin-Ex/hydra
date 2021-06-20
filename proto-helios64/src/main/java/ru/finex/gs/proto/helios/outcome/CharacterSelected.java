/*
 * This file is part of the L2J Mobius project.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ru.finex.gs.proto.helios.outcome;

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
 * @author finfan
 */
@Data
@OutcomePacket(@Opcode(0x0B))
public class CharacterSelected extends L2GameServerPacket {
	private int sessionId;
	private int runtimeId;
	private SelectedAvatarDto avatar;

	@Override
	protected void writeImpl() {
		writeC(0x0B);

		PlayerEntity player = avatar.getPlayer();
		ClanEntity clan = avatar.getClan();
		PositionEntity position = avatar.getPosition();
		StatusEntity status = avatar.getStatus();

		writeS(player.getName());
		writeD(runtimeId);
		writeS(player.getTitle());
		writeD(sessionId);
		writeD(clan.getPersistenceId());
		writeD(0x00); // ??
		writeD(player.getGender().ordinal());
		writeD(player.getRace().ordinal());
		writeD(player.getAppearanceClass().getNetworkId(player.getRace()));
		writeD(0x01); // active ??
		writeD((int) position.getX());
		writeD((int) position.getY());
		writeD((int) position.getZ());
		writeF(status.getHp());
		writeF(status.getMp());
		writeQ(0x00); // sp
		writeQ(0x00); // exp
		writeD(1); // level
		writeD(0x00); // reputation
		writeD(0); // pk kills
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
