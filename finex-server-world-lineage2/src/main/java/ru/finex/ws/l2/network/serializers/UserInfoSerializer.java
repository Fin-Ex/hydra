package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.component.player.CollisionComponent;
import ru.finex.ws.l2.component.player.RecommendationComponent;
import ru.finex.ws.l2.component.player.SpeedComponent;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.model.entity.ClanEntity;
import ru.finex.ws.l2.model.entity.ParameterEntity;
import ru.finex.ws.l2.model.entity.PlayerEntity;
import ru.finex.ws.l2.model.entity.PositionEntity;
import ru.finex.ws.l2.model.entity.StatEntity;
import ru.finex.ws.l2.model.entity.StatusEntity;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.UserInfoType;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x32))
public class UserInfoSerializer implements PacketSerializer<UserInfoDto> {

    private static final byte[] MASKS = new byte[] {
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00
    };

    private static final int size = 5;
    
    private boolean containsMask(UserInfoType userInfoType) {
        return true;
    }

    @Override
    public void serialize(UserInfoDto dto, ByteBuf buffer) {
        buffer.writeIntLE(dto.getRuntimeId());
        buffer.writeIntLE(size);
        buffer.writeShortLE(23);
        buffer.writeBytes(MASKS);

        PlayerEntity player = dto.getPlayerComponent().getEntity();

        if (containsMask(UserInfoType.RELATION)) {
            buffer.writeIntLE(0x00);
        }

        if (containsMask(UserInfoType.BASIC_INFO)) {
            buffer.writeShortLE(16 + (player.getName().length() * 2)); //get().getVisibleName()
            SerializerHelper.writeStringNullTerm(buffer, player.getName());
            buffer.writeByte(0x00); // isGM
            buffer.writeByte(player.getRace().ordinal());
            buffer.writeByte(player.getGender().ordinal());
            buffer.writeIntLE(player.getAppearanceClass().getNetworkId(player.getRace()));
            buffer.writeIntLE(ClassId.HumanFighter.ordinal());
            buffer.writeByte(1); // level
        }

        if (containsMask(UserInfoType.BASE_STATS)) {
            buffer.writeShortLE(18);
            ParameterEntity parameters = dto.getParameterComponent().getEntity();
            buffer.writeShortLE(parameters.getSTR());
            buffer.writeShortLE(parameters.getDEX());
            buffer.writeShortLE(parameters.getCON());
            buffer.writeShortLE(parameters.getINT());
            buffer.writeShortLE(parameters.getWIT());
            buffer.writeShortLE(parameters.getMEN());
            buffer.writeShortLE(parameters.getLUC());
            buffer.writeShortLE(parameters.getCHA());
        }

        if (containsMask(UserInfoType.MAX_HPCPMP)) {
            buffer.writeShortLE(14);
            StatusEntity status = dto.getStatusComponent().getStatusEntity();
            buffer.writeIntLE((int)status.getMaxHp());
            buffer.writeIntLE((int)status.getMaxMp());
            buffer.writeIntLE((int)status.getMaxCp());
        }

        if (containsMask(UserInfoType.CURRENT_HPMPCP_EXP_SP)) {
            buffer.writeShortLE(38);
            StatusEntity status = dto.getStatusComponent().getStatusEntity();
            buffer.writeIntLE((int) Math.round(status.getHp()));
            buffer.writeIntLE((int) Math.round(status.getMp()));
            buffer.writeIntLE((int) Math.round(status.getCp()));
            buffer.writeLongLE(0L); // sp
            buffer.writeLongLE(0L); // exp
            buffer.writeLongLE(Double.doubleToLongBits(0f)); // (float) (_player.getExp() - ExperienceData.getInstance().getExpForLevel(_player.getLevel())) / (ExperienceData.getInstance().getExpForLevel(_player.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(_player.getLevel()))
        }

        if (containsMask(UserInfoType.ENCHANTLEVEL)) {
            buffer.writeShortLE(4);
            buffer.writeByte(0); // _enchantLevel
            buffer.writeByte(0); // _armorEnchant
        }

        if (containsMask(UserInfoType.APPAREANCE)) {
            buffer.writeShortLE(15);
            buffer.writeIntLE(player.getHairType()); // visual hair type
            buffer.writeIntLE(player.getHairColor()); // visual haoir color
            buffer.writeIntLE(player.getFaceType()); //visual face type
            buffer.writeByte(0x00); //isHairAccessoryEnabled
        }

        if (containsMask(UserInfoType.STATUS)) {
            buffer.writeShortLE(6);
            buffer.writeByte(dto.getMountComponent().getMountType().ordinal());
            buffer.writeByte(dto.getStoreComponent().getStoreType().ordinal());
            buffer.writeByte(0x00); // _player.hasDwarvenCraft() || (_player.getSkillLevel(248) > 0) ? 1 : 0
            buffer.writeByte(0x00); // _player.getAbilityPointsUsed()
        }

        if (containsMask(UserInfoType.STATS)) {
            buffer.writeShortLE(56);
            StatEntity stat = dto.getStatComponent().getEntity();
            buffer.writeShortLE(40); // stat.getActiveWeaponItem() != null ? 40 : 20
            buffer.writeIntLE(stat.getPAtk());
            buffer.writeIntLE(stat.getAttackSpeed());
            buffer.writeIntLE(stat.getPDef());
            buffer.writeIntLE(stat.getEvasion());
            buffer.writeIntLE(stat.getAccuracy());
            buffer.writeIntLE(stat.getCriticalRate());
            buffer.writeIntLE(stat.getMAtk());
            buffer.writeIntLE(stat.getCastSpeed());
            buffer.writeIntLE(stat.getAttackSpeed()); // Seems like atk speed - 1
            buffer.writeIntLE(stat.getMagicEvasion());
            buffer.writeIntLE(stat.getMDef());
            buffer.writeIntLE(stat.getMagicAccuracy());
            buffer.writeIntLE(stat.getMagicCriticalRate());
        }

        if (containsMask(UserInfoType.ELEMENTALS)) {
            buffer.writeShortLE(14);
            buffer.writeShortLE(0x00); // defense attribute FIRE
            buffer.writeShortLE(0x00); // defense attribute WATER
            buffer.writeShortLE(0x00); // defense attribute WIND
            buffer.writeShortLE(0x00); // defense attribute EARTH
            buffer.writeShortLE(0x00); // defense attribute HOLY
            buffer.writeShortLE(0x00); // defense attribute DARK
        }

        if (containsMask(UserInfoType.POSITION)) {
            buffer.writeShortLE(18);
            PositionEntity position = dto.getCoordinateComponent().getPosition();
            buffer.writeIntLE((int)position.getX());
            buffer.writeIntLE((int)position.getY());
            buffer.writeIntLE((int)position.getZ());
            buffer.writeIntLE(0); //_player.isInVehicle() ? _player.getVehicle().getObjectId() : 0
        }

        if (containsMask(UserInfoType.SPEED)) {
            buffer.writeShortLE(18);
            SpeedComponent speedComponent = dto.getSpeedComponent();
            buffer.writeShortLE((int) speedComponent.getRunSpeed());
            buffer.writeShortLE((int) speedComponent.getWalkSpeed());
            buffer.writeShortLE((int) speedComponent.getSwimSpeed());
            buffer.writeShortLE((int) speedComponent.getSwimSpeed());
            buffer.writeShortLE((int) speedComponent.getFlySpeed());
            buffer.writeShortLE((int) speedComponent.getFlySpeed());
            buffer.writeShortLE((int) speedComponent.getFlySpeed());
            buffer.writeShortLE((int) speedComponent.getFlySpeed());
        }

        if (containsMask(UserInfoType.MULTIPLIER)) {
            buffer.writeShortLE(18);
            SpeedComponent speedComponent = dto.getSpeedComponent();
            buffer.writeLongLE(Double.doubleToLongBits(speedComponent.getAnimMoveSpeed()));
            buffer.writeLongLE(Double.doubleToLongBits(speedComponent.getAnimAttackSpeed()));
        }

        if (containsMask(UserInfoType.COL_RADIUS_HEIGHT)) {
            buffer.writeShortLE(18);
            CollisionComponent collisionComponent = dto.getCollisionComponent();
            buffer.writeLongLE(Double.doubleToLongBits(collisionComponent.getWidth()));
            buffer.writeLongLE(Double.doubleToLongBits(collisionComponent.getHeight()));
        }

        if (containsMask(UserInfoType.ATK_ELEMENTAL)) {
			/*buffer.writeShortLE(5);
			final AttributeType attackAttribute = _player.getAttackElement();
			buffer.writeByte(attackAttribute.getClientId());
			buffer.writeShortLE(_player.getAttackElementValue(attackAttribute));*/
            buffer.writeShortLE(5);
            buffer.writeByte(0x00);
            buffer.writeShortLE(0x00);
        }

        if (containsMask(UserInfoType.CLAN)) {
            buffer.writeShortLE(32 + (player.getTitle().length() * 2));
            SerializerHelper.writeStringNullTerm(buffer, player.getTitle());
            ClanEntity clan = dto.getClanComponent().getEntity();
            buffer.writeShortLE(0x00); // pledge type
            buffer.writeIntLE(clan.getPersistenceId()); // clanId
            buffer.writeIntLE(clan.getLargeCrestId());
            buffer.writeIntLE(clan.getCrestId());
            buffer.writeIntLE(0x00); //FIXME finfan: clan.getClanPrivileges().getBitmask()
            buffer.writeByte(0x00); // FIXME finfan: isCLanLeader player.isClanLeader() ? 0x01 : 0x00
            buffer.writeIntLE(0x00); // FIXME finfan: ally id
            buffer.writeIntLE(0x00); // FIXME finfan: ally crest id
            buffer.writeByte(0x00); // FIXME finfan: clan.isInMatchingRoom() ? 0x01 : 0x00
        }

        if (containsMask(UserInfoType.SOCIAL)) {
            buffer.writeShortLE(22);
            RecommendationComponent recommendationComponent = dto.getRecommendationComponent();
            buffer.writeByte(0x00); // FIXME finfan: pvp flag
            buffer.writeIntLE(0x00); // FIXME finfan: Reputation
            buffer.writeByte(0x00); // FIXME finfan: noble level
            buffer.writeByte(0); //FIXME finfan: _player.isHero() || (_player.isGM() && Config.GM_HERO_AURA) ? 1 : 0
            buffer.writeByte(0x00); // FIXME finfan: pledge class
            buffer.writeIntLE(0); // FIXME finfan: pkkills
            buffer.writeIntLE(0); // FIXME finfan: pvpkills
            buffer.writeShortLE(recommendationComponent.getLeft());
            buffer.writeShortLE(recommendationComponent.getCollect());
        }

        if (containsMask(UserInfoType.VITA_FAME)) {
            buffer.writeShortLE(15);
            buffer.writeIntLE(0); // FIXME finfan: _player.getVitalityPoints()
            buffer.writeByte(0x00); // FIXME finfan: Vita Bonus
            buffer.writeIntLE(0x00); // FIXME finfan: _player.getFame()
            buffer.writeIntLE(0x00); // FIXME finfan: _player.getRaidPoints()
        }

        if (containsMask(UserInfoType.SLOTS)) {
            buffer.writeShortLE(9);
            buffer.writeByte(0x00); // FIXME finfan: _player.getInventory().getTalismanSlots()
            buffer.writeByte(0x00); // FIXME finfan: _player.getInventory().getBroochJewelSlots()
            buffer.writeByte(0x00); // FIXME finfan: Confirmed _player.getTeam().getId()
            buffer.writeByte(0x00); // FIXME finfan: (1 = Red, 2 = White, 3 = White Pink) dotted ring on the floor
            buffer.writeByte(0x00);
            buffer.writeByte(0x00);
            buffer.writeByte(0x00);
        }

        if (containsMask(UserInfoType.MOVEMENTS)) {
            buffer.writeShortLE(4);
            buffer.writeByte(0x00); //FIXME finfan: _player.isInsideZone(ZoneId.WATER) ? 1 : _player.isFlyingMounted() ? 2 : 0
            buffer.writeByte(dto.getStateComponent().isRunning() ? 0x01 : 0x00);
        }

        if (containsMask(UserInfoType.COLOR)) {
            buffer.writeShortLE(10);
            buffer.writeIntLE(player.getNameColor());
            buffer.writeIntLE(player.getTitleColor());
        }

        if (containsMask(UserInfoType.INVENTORY_LIMIT)) {
            buffer.writeShortLE(9);
            buffer.writeShortLE(0x00);
            buffer.writeShortLE(0x00);
            buffer.writeShortLE(0x00); // _player.getInventoryLimit()
            buffer.writeByte(0x00); // TODO: cursed weapon equipped
        }

        if (containsMask(UserInfoType.TRUE_HERO)) {
            buffer.writeShortLE(9);
            buffer.writeIntLE(0x00);
            buffer.writeShortLE(0x00);
            buffer.writeByte(0x00); //_player.isTrueHero() ? 100 : 0x00
        }
    }

}
