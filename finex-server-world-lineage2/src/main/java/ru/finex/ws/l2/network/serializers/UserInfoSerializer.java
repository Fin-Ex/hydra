package ru.finex.ws.l2.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.l2.component.player.CollisionComponent;
import ru.finex.ws.l2.component.player.RecommendationComponent;
import ru.finex.ws.l2.component.player.SpeedComponent;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.model.entity.ClanComponentEntity;
import ru.finex.ws.l2.model.entity.ParameterComponentEntity;
import ru.finex.ws.l2.model.entity.PlayerComponentEntity;
import ru.finex.ws.l2.model.entity.PositionComponentEntity;
import ru.finex.ws.l2.model.entity.StatComponentEntity;
import ru.finex.ws.l2.model.entity.StatusComponentEntity;
import ru.finex.ws.l2.network.SerializerHelper;
import ru.finex.ws.l2.network.model.UserInfoType;
import ru.finex.ws.l2.network.model.dto.UserInfoDto;

import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.util.stream.Stream;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x32))
public class UserInfoSerializer implements PacketSerializer<UserInfoDto> {

	private static final byte[] MASKS = new byte[]{
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00
	};

	private static int size = 5;

	private static final byte[] DEFAULT_FLAG_ARRAY = {
		(byte) 0x80,
		0x40,
		0x20,
		0x10,
		0x08,
		0x04,
		0x02,
		0x01
	};

	void addComponentType(PlayerComponentEntity player, UserInfoType... updateComponents) {
		for (UserInfoType component : updateComponents) {
			if (!containsMask(component.getMask())) {
				addMask(component.getMask());
				onNewMaskAdded(player, component);
			}
		}
	}

	void addMask(int mask) {
		MASKS[mask >> 3] |= DEFAULT_FLAG_ARRAY[mask & 7];
	}

	boolean containsMask(UserInfoType component) {
		return containsMask(component.getMask());
	}

	boolean containsMask(int mask) {
		return (MASKS[mask >> 3] & DEFAULT_FLAG_ARRAY[mask & 7]) != 0;
	}

	public void onNewMaskAdded(PlayerComponentEntity player, UserInfoType userInfoType) {
		calcBlockSize(player, userInfoType);
	}

	private void calcBlockSize(PlayerComponentEntity player, UserInfoType type) {
		switch (type) {
			case BASIC_INFO: {
				size += type.getBlockLength() + (player.getName().length() * 2);
				break;
			}
			case CLAN: {
				size += type.getBlockLength() + (player.getTitle().length() * 2);
				break;
			}
			default: {
				size += type.getBlockLength();
				break;
			}
		}
	}

	@Override
	public void serialize(UserInfoDto dto, ByteBuf buffer) {
		PlayerComponentEntity player = dto.getPlayerComponent().getEntity();
		addComponentType(player, UserInfoType.values());

		buffer.writeIntLE(dto.getRuntimeId() + 1);
		buffer.writeIntLE(size);
		buffer.writeShortLE(23);
		buffer.writeBytes(MASKS);

		if (containsMask(UserInfoType.RELATION)) {
			buffer.writeIntLE(0x00);
		}

		if (containsMask(UserInfoType.BASIC_INFO)) {
			buffer.writeShortLE(UserInfoType.BASIC_INFO.getBlockLength() + (player.getName().length() * 2)); //get().getVisibleName()
			SerializerHelper.writeString(buffer, player.getName());
			buffer.writeByte(0x00); // isGM
			buffer.writeByte(player.getRace().ordinal());
			buffer.writeByte(player.getGender().ordinal());
			buffer.writeIntLE(player.getAppearanceClass().getNetworkId(player.getRace()));
			buffer.writeIntLE(ClassId.HumanFighter.ordinal());
			buffer.writeByte(1); // level
		}

		if (containsMask(UserInfoType.BASE_STATS)) {
			buffer.writeShortLE(UserInfoType.BASE_STATS.getBlockLength());
			ParameterComponentEntity parameters = dto.getParameterComponent().getEntity();
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
			buffer.writeShortLE(UserInfoType.MAX_HPCPMP.getBlockLength());
			StatusComponentEntity status = dto.getStatusComponent().getStatusEntity();
			buffer.writeIntLE(status.getMaxHp().intValue());
			buffer.writeIntLE(status.getMaxMp().intValue());
			buffer.writeIntLE(status.getMaxCp().intValue());
		}

		if (containsMask(UserInfoType.CURRENT_HPMPCP_EXP_SP)) {
			buffer.writeShortLE(UserInfoType.CURRENT_HPMPCP_EXP_SP.getBlockLength());
			StatusComponentEntity status = dto.getStatusComponent().getStatusEntity();
			buffer.writeIntLE((int) Math.round(status.getHp()));
			buffer.writeIntLE((int) Math.round(status.getMp()));
			buffer.writeIntLE((int) Math.round(status.getCp()));
			buffer.writeLongLE(0L); // sp
			buffer.writeLongLE(0L); // exp
			buffer.writeLongLE(Double.doubleToLongBits(0f)); // (float) (_player.getExp() - ExperienceData.getInstance().getExpForLevel(_player.getLevel())) / (ExperienceData.getInstance().getExpForLevel(_player.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(_player.getLevel()))
		}

		if (containsMask(UserInfoType.ENCHANTLEVEL)) {
			buffer.writeShortLE(UserInfoType.ENCHANTLEVEL.getBlockLength());
			buffer.writeByte(0); // _enchantLevel
			buffer.writeByte(0); // _armorEnchant
		}

		if (containsMask(UserInfoType.APPAREANCE)) {
			buffer.writeShortLE(UserInfoType.APPAREANCE.getBlockLength());
			buffer.writeIntLE(player.getHairType()); // visual hair type
			buffer.writeIntLE(player.getHairColor()); // visual haoir color
			buffer.writeIntLE(player.getFaceType()); //visual face type
			buffer.writeByte(0x00); //isHairAccessoryEnabled
		}

		if (containsMask(UserInfoType.STATUS)) {
			buffer.writeShortLE(UserInfoType.STATUS.getBlockLength());
			buffer.writeByte(dto.getMountComponent().getMountType().ordinal());
			buffer.writeByte(dto.getStoreComponent().getStoreType().ordinal());
			buffer.writeByte(0x00); // _player.hasDwarvenCraft() || (_player.getSkillLevel(248) > 0) ? 1 : 0
			buffer.writeByte(0x00); // _player.getAbilityPointsUsed()
		}

		if (containsMask(UserInfoType.STATS)) {
			buffer.writeShortLE(UserInfoType.STATS.getBlockLength());
			StatComponentEntity stat = dto.getStatComponent().getEntity();
			buffer.writeShortLE(20); // stat.getActiveWeaponItem() != null ? 40 : 20
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
			buffer.writeShortLE(UserInfoType.ELEMENTALS.getBlockLength());
			buffer.writeShortLE(0x00); // defense attribute FIRE
			buffer.writeShortLE(0x00); // defense attribute WATER
			buffer.writeShortLE(0x00); // defense attribute WIND
			buffer.writeShortLE(0x00); // defense attribute EARTH
			buffer.writeShortLE(0x00); // defense attribute HOLY
			buffer.writeShortLE(0x00); // defense attribute DARK
		}

		if (containsMask(UserInfoType.POSITION)) {
			buffer.writeShortLE(UserInfoType.POSITION.getBlockLength());
			PositionComponentEntity position = dto.getCoordinateComponent().getPosition();
			buffer.writeIntLE(position.getX().intValue());
			buffer.writeIntLE(position.getY().intValue());
			buffer.writeIntLE(position.getZ().intValue());
			buffer.writeIntLE(0); //_player.isInVehicle() ? _player.getVehicle().getObjectId() : 0
		}

		if (containsMask(UserInfoType.SPEED)) {
			buffer.writeShortLE(UserInfoType.SPEED.getBlockLength());
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
			buffer.writeShortLE(UserInfoType.MULTIPLIER.getBlockLength());
			SpeedComponent speedComponent = dto.getSpeedComponent();
			buffer.writeLongLE(Double.doubleToLongBits(speedComponent.getAnimMoveSpeed()));
			buffer.writeLongLE(Double.doubleToLongBits(speedComponent.getAnimAttackSpeed()));
		}

		if (containsMask(UserInfoType.COL_RADIUS_HEIGHT)) {
			buffer.writeShortLE(UserInfoType.COL_RADIUS_HEIGHT.getBlockLength());
			CollisionComponent collisionComponent = dto.getCollisionComponent();
			buffer.writeLongLE(Double.doubleToLongBits(collisionComponent.getWidth()));
			buffer.writeLongLE(Double.doubleToLongBits(collisionComponent.getHeight()));
		}

		if (containsMask(UserInfoType.ATK_ELEMENTAL)) {
			/*buffer.writeShortLE(5);
			final AttributeType attackAttribute = _player.getAttackElement();
			buffer.writeByte(attackAttribute.getClientId());
			buffer.writeShortLE(_player.getAttackElementValue(attackAttribute));*/
			buffer.writeShortLE(UserInfoType.ATK_ELEMENTAL.getBlockLength());
			buffer.writeByte(-0x02); // None
			buffer.writeShortLE(0x00);
		}

		if (containsMask(UserInfoType.CLAN)) {
			buffer.writeShortLE(UserInfoType.CLAN.getBlockLength() + (player.getTitle().length() * 2));
			SerializerHelper.writeString(buffer, player.getTitle());
			ClanComponentEntity clan = dto.getClanComponent().getEntity();
			buffer.writeShortLE(0x00); // pledge type
			buffer.writeIntLE(clan.getPersistenceId()); // clanId
			buffer.writeIntLE(clan.getPersistenceId()); // large crest id
			buffer.writeIntLE(clan.getPersistenceId()); // crest id
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
			buffer.writeShortLE(UserInfoType.SLOTS.getBlockLength());
			buffer.writeByte(0x00); // FIXME finfan: _player.getInventory().getTalismanSlots()
			buffer.writeByte(0x00); // FIXME finfan: _player.getInventory().getBroochJewelSlots()
			buffer.writeByte(0x00); // FIXME finfan: Confirmed _player.getTeam().getId()
			buffer.writeByte(0x00); // FIXME finfan: (1 = Red, 2 = White, 3 = White Pink) dotted ring on the floor
			buffer.writeByte(0x00);
			buffer.writeByte(0x00);
			buffer.writeByte(0x00);
		}

		if (containsMask(UserInfoType.MOVEMENTS)) {
			buffer.writeShortLE(UserInfoType.MOVEMENTS.getBlockLength());
			buffer.writeByte(0x00); //FIXME finfan: _player.isInsideZone(ZoneId.WATER) ? 1 : _player.isFlyingMounted() ? 2 : 0
			buffer.writeByte(dto.getStateComponent().isRunning() ? 0x01 : 0x00);
		}

		if (containsMask(UserInfoType.COLOR)) {
			buffer.writeShortLE(UserInfoType.COLOR.getBlockLength());
			buffer.writeIntLE(player.getNameColor());
			buffer.writeIntLE(player.getTitleColor());
		}

		if (containsMask(UserInfoType.INVENTORY_LIMIT)) {
			buffer.writeShortLE(UserInfoType.INVENTORY_LIMIT.getBlockLength());
			buffer.writeShortLE(0x00);
			buffer.writeShortLE(0x00);
			buffer.writeShortLE(0x00); // _player.getInventoryLimit()
			buffer.writeByte(0x00); // TODO: cursed weapon equipped
		}

		if (containsMask(UserInfoType.TRUE_HERO)) {
			buffer.writeShortLE(UserInfoType.TRUE_HERO.getBlockLength());
			buffer.writeIntLE(0x00);
			buffer.writeShortLE(0x00);
			buffer.writeByte(0x00); //_player.isTrueHero() ? 100 : 0x00
		}
	}

}
