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
import ru.finex.gs.model.dto.LobbyAvatarDto;
import ru.finex.gs.model.entity.ClanEntity;
import ru.finex.gs.model.entity.PlayerEntity;
import ru.finex.gs.model.entity.PositionEntity;
import ru.finex.gs.model.entity.StatusEntity;
import ru.finex.gs.proto.network.L2GameServerPacket;
import ru.finex.gs.proto.network.Opcode;
import ru.finex.gs.proto.network.OutcomePacket;
import sf.l2j.gameserver.model.base.ClassId;

import java.util.List;

/**
 * @author finfan
 */
@Data
@OutcomePacket(@Opcode(0x0B))
public class CharSelectionInfo extends L2GameServerPacket {

	private String login;
	private int sessionId;
	private List<LobbyAvatarDto> avatars;

	@Override
	protected void writeImpl() {
		writeC(0x09);
		writeD(avatars.size()); // Created character count
		writeD(7); // Can prevent players from creating new characters (if 0); (if 1, the client will ask if chars may be created (0x13) Response: (0x0D) )
		writeC(0x00); // if 1 can't create new char
		writeC(0x02); // 0=can't play, 1=can play free until level 85, 2=100% free play
		writeD(0x02); // if 0x01, Korean client
		writeC(0x00); // If 0x01 suggests premium account

		/*long lastAccess = 0;
		if (_activeId == -1) {
			for (int i = 0; i < size; i++) {
				if (lastAccess < _characterPackages[i].getLastAccess()) {
					lastAccess = _characterPackages[i].getLastAccess();
					_activeId = i;
				}
			}
		}*/

		for (LobbyAvatarDto avatar : avatars) {
			PlayerEntity player = avatar.getPlayer();
			ClanEntity clan = avatar.getClan();
			PositionEntity position = avatar.getPosition();
			StatusEntity status = avatar.getStatus();

			writeS(player.getName()); // Character name
			writeD(player.getPersistenceId()); // Character ID
			writeS(login); // Account name
			writeD(sessionId); // Account ID
			writeD(0x00); // Clan ID
			writeD(0x00); // Builder level

			writeD(player.getGender().ordinal()); // Sex
			writeD(player.getRace().ordinal()); // Race
			writeD(avatar.getPlayer().getAppearanceClass().getNetworkId(avatar.getPlayer().getRace()));

			writeD(0x01); // GameServerName

			writeD((int) position.getX());
			writeD((int) position.getY());
			writeD((int) position.getZ());
			writeF(status.getHp());
			writeF(status.getMp());

			writeQ(0x00); // sp
			writeQ(0x00); // exp

			writeF(0f); //(float) (charInfoPackage.getExp() - ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel())) / (ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel()))

			writeD(1); // level
			writeD(0); // reputation
			writeD(0); // pkkills
			writeD(0); // pvpkills

			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);

			writeD(0x00); // Ertheia
			writeD(0x00); // Ertheia

			for (int slot = 0; slot < 32; slot++) {
				writeD(0x00);
			}

			for (int slot = 0; slot < 9; slot++) {
				writeD(0x00);
				//FIXME finfan: тут используем следующие слоты следуя L2JMobius
				/*
					private static final int[] PAPERDOLL_ORDER_VISUAL_ID = new int[] {
						Inventory.PAPERDOLL_RHAND,
						Inventory.PAPERDOLL_LHAND,
						Inventory.PAPERDOLL_GLOVES,
						Inventory.PAPERDOLL_CHEST,
						Inventory.PAPERDOLL_LEGS,
						Inventory.PAPERDOLL_FEET,
						Inventory.PAPERDOLL_RHAND, <-- что это за хуета? дупликат? должно быть LRHAND?
						Inventory.PAPERDOLL_HAIR,
						Inventory.PAPERDOLL_HAIR2,
					};
				 */
			}

			writeH(0x00); // Upper Body enchant level
			writeH(0x00); // Lower Body enchant level
			writeH(0x00); // Headgear enchant level
			writeH(0x00); // Gloves enchant level
			writeH(0x00); // Boots enchant level

			writeD(player.getHairType());
			writeD(player.getHairColor());
			writeD(player.getFaceType());

			writeF(status.getMaxHp()); // Maximum HP
			writeF(status.getMaxMp()); // Maximum MP

			writeD(0x00); // charInfoPackage.getDeleteTimer() > 0 ? (int) ((charInfoPackage.getDeleteTimer() - System.currentTimeMillis()) / 1000) : 0
			writeD(ClassId.HumanFighter.ordinal());
			writeD(0x01);  // selected avatar or not

			writeC(0); // enchant weapon
			writeD(0); // augmentation id 1
			writeD(0); // augmentation id 2

			// writeD(charInfoPackage.getTransformId()); // Used to display Transformations
			writeD(0x00); // Currently on retail when you are on character select you don't see your transformation.

			writeD(0x00); // Pet NpcId
			writeD(0x00); // Pet level
			writeD(0x00); // Pet Food
			writeD(0x00); // Pet Food Level
			writeF(0x00); // Current pet HP
			writeF(0x00); // Current pet MP

			writeD(0); // Vitality points
			writeD(0); // Vitality Percent, finfan: was (int) Config.RATE_VITALITY_EXP_MULTIPLIER * 100
			writeD(0x00); // Remaining vitality item uses finfan: was charInfoPackage.getVitalityItemsUsed()
			writeD(0x01); // Char is active or not ???? finfan: was charInfoPackage.getAccessLevel() == -100 ? 0x00 : 0x01
			writeC(0x00); // isNoble
			writeC(0x00); // Hero glow
			writeC(0x00); // Show hair accessory if enabled finfan: was charInfoPackage.isHairAccessoryEnabled() ? 0x01 : 0x00
		}
	}
}
