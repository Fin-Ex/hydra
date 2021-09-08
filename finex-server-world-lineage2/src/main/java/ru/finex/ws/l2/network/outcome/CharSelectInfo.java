package ru.finex.ws.l2.network.outcome;

import lombok.Data;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.model.dto.LobbyAvatarDto;
import ru.finex.ws.l2.model.entity.ClanEntity;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.ws.l2.model.entity.PositionEntity;
import ru.finex.ws.l2.model.entity.StatusEntity;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.OutcomePacket;
import ru.finex.ws.l2.network.model.L2GameServerPacket;

import java.util.List;

/**
 * FIXME m0nster.mind: для данного пакета необходимо отдельно создавать GameObject со своим набором компонентов,
 *  который будет отображать аватара игрока в лобби.
 * @author m0nster.mind
 */
@Data
@OutcomePacket(@Opcode(0x09))
public class CharSelectInfo extends L2GameServerPacket {

	private String login;
	private int sessionId;
	private List<LobbyAvatarDto> avatars;

	@Override
	protected final void writeImpl() {
		writeC(0x09);
		writeD(avatars.size());
		writeD(7); // Can prevent players from creating new characters (if 0); (if 1, the client will ask if chars may be created (0x13) Response: (0x0D) )
		writeC(0x00); // if 1 can't create new char
		writeC(0x02); // 0=can't play, 1=can play free until level 85, 2=100% free play
		writeD(0x02); // if 0x01, Korean client
		writeC(0x00); // If 0x01 suggests premium account

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
