package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.model.dto.LobbyAvatarDto;
import ru.finex.ws.l2.model.entity.ClanEntity;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.ws.l2.model.entity.PositionEntity;
import ru.finex.ws.l2.model.entity.StatusEntity;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.dto.CharSelectInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x09))
public class CharSelectInfoSerializer implements PacketSerializer<CharSelectInfoDto> {
    
    @Override
    public void serialize(CharSelectInfoDto dto, ByteBuf buffer) {
        var avatars = dto.getAvatars();

        buffer.writeIntLE(avatars.size());
        buffer.writeIntLE(7); // Can prevent players from creating new characters (if 0); (if 1, the client will ask if chars may be created (0x13) Response: (0x0D) )
        buffer.writeByte(0x00); // if 1 can't create new char
        buffer.writeByte(0x02); // 0=can't play, 1=can play free until level 85, 2=100% free play
        buffer.writeIntLE(0x02); // if 0x01, Korean client
        buffer.writeByte(0x00); // If 0x01 suggests premium account

        for (LobbyAvatarDto avatar : avatars) {
            PlayerEntity player = avatar.getPlayer();
            ClanEntity clan = avatar.getClan();
            PositionEntity position = avatar.getPosition();
            StatusEntity status = avatar.getStatus();

            SerializerHelper.writeStringNullTerm(buffer, player.getName()); // Character name
            buffer.writeIntLE(player.getPersistenceId()); // Character ID
            SerializerHelper.writeStringNullTerm(buffer, dto.getLogin()); // Account name
            buffer.writeIntLE(dto.getSessionId()); // Account ID
            buffer.writeIntLE(0x00); // Clan ID
            buffer.writeIntLE(0x00); // Builder level

            buffer.writeIntLE(player.getGender().ordinal()); // Sex
            buffer.writeIntLE(player.getRace().ordinal()); // Race
            buffer.writeIntLE(avatar.getPlayer().getAppearanceClass().getNetworkId(avatar.getPlayer().getRace()));

            buffer.writeIntLE(0x01); // GameServerName

            buffer.writeIntLE((int) position.getX());
            buffer.writeIntLE((int) position.getY());
            buffer.writeIntLE((int) position.getZ());
            buffer.writeLongLE(Double.doubleToLongBits(status.getHp()));
            buffer.writeLongLE(Double.doubleToLongBits(status.getMp()));

            buffer.writeLongLE(0x00); // sp
            buffer.writeLongLE(0x00); // exp

            buffer.writeLongLE(Double.doubleToLongBits(0f)); //(float) (charInfoPackage.getExp() - ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel())) / (ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel()))

            buffer.writeIntLE(1); // level
            buffer.writeIntLE(0); // reputation
            buffer.writeIntLE(0); // pkkills
            buffer.writeIntLE(0); // pvpkills

            buffer.writeIntLE(0x00);
            buffer.writeIntLE(0x00);
            buffer.writeIntLE(0x00);
            buffer.writeIntLE(0x00);
            buffer.writeIntLE(0x00);
            buffer.writeIntLE(0x00);
            buffer.writeIntLE(0x00);

            buffer.writeIntLE(0x00); // Ertheia
            buffer.writeIntLE(0x00); // Ertheia

            for (int slot = 0; slot < 32; slot++) {
                buffer.writeIntLE(0x00);
            }

            for (int slot = 0; slot < 9; slot++) {
                buffer.writeIntLE(0x00);
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

            buffer.writeShortLE(0x00); // Upper Body enchant level
            buffer.writeShortLE(0x00); // Lower Body enchant level
            buffer.writeShortLE(0x00); // Headgear enchant level
            buffer.writeShortLE(0x00); // Gloves enchant level
            buffer.writeShortLE(0x00); // Boots enchant level

            buffer.writeIntLE(player.getHairType());
            buffer.writeIntLE(player.getHairColor());
            buffer.writeIntLE(player.getFaceType());

            buffer.writeLongLE(Double.doubleToLongBits(status.getMaxHp())); // Maximum HP
            buffer.writeLongLE(Double.doubleToLongBits(status.getMaxMp())); // Maximum MP

            buffer.writeIntLE(0x00); // charInfoPackage.getDeleteTimer() > 0 ? (int) ((charInfoPackage.getDeleteTimer() - System.currentTimeMillis()) / 1000) : 0
            buffer.writeIntLE(ClassId.HumanFighter.ordinal());
            buffer.writeIntLE(0x01);  // selected avatar or not

            buffer.writeByte(0); // enchant weapon
            buffer.writeIntLE(0); // augmentation id 1
            buffer.writeIntLE(0); // augmentation id 2

            // buffer.writeIntLE(charInfoPackage.getTransformId()); // Used to display Transformations
            buffer.writeIntLE(0x00); // Currently on retail when you are on character select you don't see your transformation.

            buffer.writeIntLE(0x00); // Pet NpcId
            buffer.writeIntLE(0x00); // Pet level
            buffer.writeIntLE(0x00); // Pet Food
            buffer.writeIntLE(0x00); // Pet Food Level
            buffer.writeLongLE(Double.doubleToLongBits(0x00)); // Current pet HP
            buffer.writeLongLE(Double.doubleToLongBits(0x00)); // Current pet MP

            buffer.writeIntLE(0); // Vitality points
            buffer.writeIntLE(0); // Vitality Percent, finfan: was (int) Config.RATE_VITALITY_EXP_MULTIPLIER * 100
            buffer.writeIntLE(0x00); // Remaining vitality item uses finfan: was charInfoPackage.getVitalityItemsUsed()
            buffer.writeIntLE(0x01); // Char is active or not ???? finfan: was charInfoPackage.getAccessLevel() == -100 ? 0x00 : 0x01
            buffer.writeByte(0x00); // isNoble
            buffer.writeByte(0x00); // Hero glow
            buffer.writeByte(0x00); // Show hair accessory if enabled finfan: was charInfoPackage.isHairAccessoryEnabled() ? 0x01 : 0x00
        }
    }
    
}
