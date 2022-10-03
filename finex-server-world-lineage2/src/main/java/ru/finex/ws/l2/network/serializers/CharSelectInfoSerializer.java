package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.model.entity.AvatarView;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.dto.CharSelectInfoDto;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
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

        for (AvatarView avatar : avatars) {
            SerializerHelper.writeStringNullTerm(buffer, avatar.getName()); // Character name
            buffer.writeIntLE(avatar.getPersistenceId()); // Character ID
            SerializerHelper.writeStringNullTerm(buffer, avatar.getLogin()); // Account name
            buffer.writeIntLE(dto.getSessionId()); // Account ID
            buffer.writeIntLE(avatar.getClanId()); // Clan ID
            buffer.writeIntLE(avatar.getBuilderLevel()); // Builder level

            buffer.writeIntLE(avatar.getGender().ordinal()); // Sex
            buffer.writeIntLE(avatar.getRace().ordinal()); // Race
            buffer.writeIntLE(avatar.getAppearanceClass().getNetworkId(avatar.getRace()));

            buffer.writeIntLE(0x01); // GameServerName

            buffer.writeIntLE(avatar.getX().intValue());
            buffer.writeIntLE(avatar.getY().intValue());
            buffer.writeIntLE(avatar.getZ().intValue());
            buffer.writeLongLE(Double.doubleToLongBits(avatar.getHp()));
            buffer.writeLongLE(Double.doubleToLongBits(avatar.getMp()));

            buffer.writeLongLE(avatar.getSp()); // sp
            buffer.writeLongLE(avatar.getExp()); // exp

            buffer.writeLongLE(Double.doubleToLongBits(avatar.getExpPercent())); //(float) (charInfoPackage.getExp() - ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel())) / (ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel()))

            buffer.writeIntLE(avatar.getLevel()); // level
            buffer.writeIntLE(avatar.getReputation()); // reputation
            buffer.writeIntLE(avatar.getPkCount()); // pkkills
            buffer.writeIntLE(avatar.getPvpCount()); // pvpkills

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

            buffer.writeIntLE(avatar.getHairType());
            buffer.writeIntLE(avatar.getHairColor());
            buffer.writeIntLE(avatar.getFaceType());

            buffer.writeLongLE(Double.doubleToLongBits(avatar.getMaxHp())); // Maximum HP
            buffer.writeLongLE(Double.doubleToLongBits(avatar.getMaxMp())); // Maximum MP

            long remainsSecondsToDelete = Optional.ofNullable(avatar.getDeleteDate())
                    .map(deleteDate -> Duration.between(Instant.now(), avatar.getDeleteDate())
                        .toSeconds()
                    ).orElse(0L);
            buffer.writeIntLE((int) remainsSecondsToDelete);
            buffer.writeIntLE(ClassId.HumanFighter.ordinal());
            buffer.writeIntLE(0x01);  // selected avatar or not

            buffer.writeByte(0); // enchant weapon
            buffer.writeIntLE(0); // augmentation id 1
            buffer.writeIntLE(0); // augmentation id 2

            // buffer.writeIntLE(charInfoPackage.getTransformId()); // Used to display Transformations
            buffer.writeIntLE(0x00); // Currently on retail when you are on character select you don't see your transformation.

            buffer.writeIntLE(avatar.getPetPrototypeId()); // Pet NpcId
            buffer.writeIntLE(avatar.getPetLevel()); // Pet level
            buffer.writeIntLE(avatar.getPetFood()); // Pet Food
            buffer.writeIntLE(avatar.getPetFoodLevel()); // Pet Food Level
            buffer.writeLongLE(Double.doubleToLongBits(avatar.getPetHp())); // Current pet HP
            buffer.writeLongLE(Double.doubleToLongBits(avatar.getPetMp())); // Current pet MP

            buffer.writeIntLE(avatar.getVitalityCount()); // Vitality points
            buffer.writeIntLE(avatar.getVitalityPercent()); // Vitality Percent, finfan: was (int) Config.RATE_VITALITY_EXP_MULTIPLIER * 100
            buffer.writeIntLE(avatar.getVitalityItemsUsed()); // Remaining vitality item uses finfan: was charInfoPackage.getVitalityItemsUsed()
            buffer.writeIntLE(avatar.getIsAccessible() ? 0x01 : 0x00); // Char is active or not ???? finfan: was charInfoPackage.getAccessLevel() == -100 ? 0x00 : 0x01
            buffer.writeByte(avatar.getIsNoble() ? 0x01 : 0x00); // isNoble
            buffer.writeByte(avatar.getIsHero() ? 0x01 : 0x00); // Hero glow
            buffer.writeByte(avatar.getIsShowHairAccessory() ? 0x01 : 0x00); // Show hair accessory if enabled finfan: was charInfoPackage.isHairAccessoryEnabled() ? 0x01 : 0x00
        }
    }
    
}
