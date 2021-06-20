package ru.finex.gs.proto.helios.outcome;

import lombok.Data;
import ru.finex.gs.model.component.base.CoordinateComponent;
import ru.finex.gs.model.component.base.ParameterComponent;
import ru.finex.gs.model.component.base.StatComponent;
import ru.finex.gs.model.component.base.StatusComponent;
import ru.finex.gs.model.component.player.AbnormalComponent;
import ru.finex.gs.model.component.player.ClanComponent;
import ru.finex.gs.model.component.player.ClassComponent;
import ru.finex.gs.model.component.player.CollisionComponent;
import ru.finex.gs.model.component.player.CubicComponent;
import ru.finex.gs.model.component.player.MountComponent;
import ru.finex.gs.model.component.player.PlayerComponent;
import ru.finex.gs.model.component.player.RecommendationComponent;
import ru.finex.gs.model.component.player.SpeedComponent;
import ru.finex.gs.model.component.player.StateComponent;
import ru.finex.gs.model.component.player.StoreComponent;
import ru.finex.gs.model.entity.ClanEntity;
import ru.finex.gs.model.entity.ParameterEntity;
import ru.finex.gs.model.entity.PlayerEntity;
import ru.finex.gs.model.entity.PositionEntity;
import ru.finex.gs.model.entity.StatEntity;
import ru.finex.gs.model.entity.StatusEntity;
import ru.finex.gs.proto.UserInfoType;
import ru.finex.gs.proto.network.Opcode;
import ru.finex.gs.proto.network.OutcomePacket;
import sf.l2j.gameserver.model.base.ClassId;

/**
 * @author finfan
 */
@Data
//FIXME finfan: какие опкоды принимает юзер инфо? типо мы отслыаем только то что нам нужно? по типу?
@OutcomePacket(@Opcode(0x32))
public class UserInfo extends AbstractMaskPacket<UserInfoType> {

	private static final byte[] MASKS = new byte[] {
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00
	};

	private int runtimeId;
	private PlayerComponent playerComponent;
	private CollisionComponent collisionComponent;
	private SpeedComponent speedComponent;
	private CubicComponent cubicComponent;
	private StateComponent stateComponent;
	private AbnormalComponent abnormalComponent;
	private ClanComponent clanComponent;
	private RecommendationComponent recommendationComponent;
	private MountComponent mountComponent;
	private ClassComponent classComponent;
	private StoreComponent storeComponent;
	private CoordinateComponent coordinateComponent;
	private StatusComponent statusComponent;
	private ParameterComponent parameterComponent;
	private StatComponent statComponent;

	private UserInfoType infoType;
	private int size = 5;

	//FIXME говно от mobius, что эт оя хз, но примерно думаю, что это некий размер отправляемого инфо компонента который должен пересчитываться?
	private void calcBlockSize(UserInfoType type) {
		switch (type) {
			case BASIC_INFO: {
				size += type.getBlockLength() + (playerComponent.getEntity().getName().length() * 2);
				break;
			}
			case CLAN: {
				size += type.getBlockLength() + (playerComponent.getEntity().getTitle().length() * 2);
				break;
			}
			default: {
				size += type.getBlockLength();
				break;
			}
		}
	}

	@Override
	protected void writeImpl() {
		writeC(0x32);
		writeD(runtimeId);
		writeD(size);
		writeH(23);
		writeB(MASKS);

		PlayerEntity player = playerComponent.getEntity();

		if (containsMask(UserInfoType.RELATION)) {
			writeD(0x00);
		}

		if (containsMask(UserInfoType.BASIC_INFO)) {
			writeH(16 + (player.getName().length() * 2)); //get().getVisibleName()
			writeS(player.getName());
			writeC(0x00); // isGM
			writeC(player.getRace().ordinal());
			writeC(player.getGender().ordinal());
			writeD(player.getAppearanceClass().getNetworkId(player.getRace()));
			writeD(ClassId.HumanFighter.ordinal());
			writeC(1); // level
		}

		if (containsMask(UserInfoType.BASE_STATS)) {
			writeH(18);
			ParameterEntity parameters = parameterComponent.getEntity();
			writeH(parameters.getSTR());
			writeH(parameters.getDEX());
			writeH(parameters.getCON());
			writeH(parameters.getINT());
			writeH(parameters.getWIT());
			writeH(parameters.getMEN());
			writeH(parameters.getLUC());
			writeH(parameters.getCHA());
		}

		if (containsMask(UserInfoType.MAX_HPCPMP)) {
			writeH(14);
			StatusEntity status = statusComponent.getStatusEntity();
			writeD((int)status.getMaxHp());
			writeD((int)status.getMaxMp());
			writeD((int)status.getMaxCp());
		}

		if (containsMask(UserInfoType.CURRENT_HPMPCP_EXP_SP)) {
			writeH(38);
			StatusEntity status = statusComponent.getStatusEntity();
			writeD((int) Math.round(status.getHp()));
			writeD((int) Math.round(status.getMp()));
			writeD((int) Math.round(status.getCp()));
			writeQ(0L); // sp
			writeQ(0L); // exp
			writeF(0f); // (float) (_player.getExp() - ExperienceData.getInstance().getExpForLevel(_player.getLevel())) / (ExperienceData.getInstance().getExpForLevel(_player.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(_player.getLevel()))
		}

		if (containsMask(UserInfoType.ENCHANTLEVEL)) {
			writeH(4);
			writeC(0); // _enchantLevel
			writeC(0); // _armorEnchant
		}

		if (containsMask(UserInfoType.APPAREANCE)) {
			writeH(15);
			writeD(player.getHairType()); // visual hair type
			writeD(player.getHairColor()); // visual haoir color
			writeD(player.getFaceType()); //visual face type
			writeC(0x00); //isHairAccessoryEnabled
		}

		if (containsMask(UserInfoType.STATUS)) {
			writeH(6);
			writeC(mountComponent.getMountType().ordinal());
			writeC(storeComponent.getStoreType().ordinal());
			writeC(0x00); // _player.hasDwarvenCraft() || (_player.getSkillLevel(248) > 0) ? 1 : 0
			writeC(0x00); // _player.getAbilityPointsUsed()
		}

		if (containsMask(UserInfoType.STATS)) {
			writeH(56);
			StatEntity stat = statComponent.getEntity();
			writeH(40); // stat.getActiveWeaponItem() != null ? 40 : 20
			writeD(stat.getPAtk());
			writeD(stat.getAttackSpeed());
			writeD(stat.getPDef());
			writeD(stat.getEvasion());
			writeD(stat.getAccuracy());
			writeD(stat.getCriticalRate());
			writeD(stat.getMAtk());
			writeD(stat.getCastSpeed());
			writeD(stat.getAttackSpeed()); // Seems like atk speed - 1
			writeD(stat.getMagicEvasion());
			writeD(stat.getMDef());
			writeD(stat.getMagicAccuracy());
			writeD(stat.getMagicCriticalRate());
		}

		if (containsMask(UserInfoType.ELEMENTALS)) {
			writeH(14);
			writeH(0x00); // defense attribute FIRE
			writeH(0x00); // defense attribute WATER
			writeH(0x00); // defense attribute WIND
			writeH(0x00); // defense attribute EARTH
			writeH(0x00); // defense attribute HOLY
			writeH(0x00); // defense attribute DARK
		}

		if (containsMask(UserInfoType.POSITION)) {
			writeH(18);
			PositionEntity position = coordinateComponent.getPosition();
			writeD((int)position.getX());
			writeD((int)position.getY());
			writeD((int)position.getZ());
			writeD(0); //_player.isInVehicle() ? _player.getVehicle().getObjectId() : 0
		}

		if (containsMask(UserInfoType.SPEED)) {
			writeH(18);
			writeH((int) speedComponent.getRunSpeed());
			writeH((int) speedComponent.getWalkSpeed());
			writeH((int) speedComponent.getSwimSpeed());
			writeH((int) speedComponent.getSwimSpeed());
			writeH((int) speedComponent.getFlySpeed());
			writeH((int) speedComponent.getFlySpeed());
			writeH((int) speedComponent.getFlySpeed());
			writeH((int) speedComponent.getFlySpeed());
		}

		if (containsMask(UserInfoType.MULTIPLIER)) {
			writeH(18);
			writeF(speedComponent.getAnimMoveSpeed());
			writeF(speedComponent.getAnimAttackSpeed());
		}

		if (containsMask(UserInfoType.COL_RADIUS_HEIGHT)) {
			writeH(18);
			writeF(collisionComponent.getWidth());
			writeF(collisionComponent.getHeight());
		}

		if (containsMask(UserInfoType.ATK_ELEMENTAL)) {
			/*writeH(5);
			final AttributeType attackAttribute = _player.getAttackElement();
			writeC(attackAttribute.getClientId());
			writeH(_player.getAttackElementValue(attackAttribute));*/
			writeH(5);
			writeC(0x00);
			writeH(0x00);
		}

		if (containsMask(UserInfoType.CLAN)) {
			writeH(32 + (player.getTitle().length() * 2));
			writeS(player.getTitle());
			ClanEntity clan = clanComponent.getEntity();
			writeH(0x00); // pledge type
			writeD(clan.getPersistenceId()); // clanId
			writeD(clan.getLargeCrestId());
			writeD(clan.getCrestId());
			writeD(0x00); //FIXME finfan: clan.getClanPrivileges().getBitmask()
			writeC(0x00); // FIXME finfan: isCLanLeader player.isClanLeader() ? 0x01 : 0x00
			writeD(0x00); // FIXME finfan: ally id
			writeD(0x00); // FIXME finfan: ally crest id
			writeC(0x00); // FIXME finfan: clan.isInMatchingRoom() ? 0x01 : 0x00
		}

		if (containsMask(UserInfoType.SOCIAL)) {
			writeH(22);
			writeC(0x00); // FIXME finfan: pvp flag
			writeD(0x00); // FIXME finfan: Reputation
			writeC(0x00); // FIXME finfan: noble level
			writeC(0); //FIXME finfan: _player.isHero() || (_player.isGM() && Config.GM_HERO_AURA) ? 1 : 0
			writeC(0x00); // FIXME finfan: pledge class
			writeD(0); // FIXME finfan: pkkills
			writeD(0); // FIXME finfan: pvpkills
			writeH(recommendationComponent.getLeft());
			writeH(recommendationComponent.getCollect());
		}

		if (containsMask(UserInfoType.VITA_FAME)) {
			writeH(15);
			writeD(0); // FIXME finfan: _player.getVitalityPoints()
			writeC(0x00); // FIXME finfan: Vita Bonus
			writeD(0x00); // FIXME finfan: _player.getFame()
			writeD(0x00); // FIXME finfan: _player.getRaidPoints()
		}

		if (containsMask(UserInfoType.SLOTS)) {
			writeH(9);
			writeC(0x00); // FIXME finfan: _player.getInventory().getTalismanSlots()
			writeC(0x00); // FIXME finfan: _player.getInventory().getBroochJewelSlots()
			writeC(0x00); // FIXME finfan: Confirmed _player.getTeam().getId()
			writeC(0x00); // FIXME finfan: (1 = Red, 2 = White, 3 = White Pink) dotted ring on the floor
			writeC(0x00);
			writeC(0x00);
			writeC(0x00);
		}

		if (containsMask(UserInfoType.MOVEMENTS)) {
			writeH(4);
			writeC(0x00); //FIXME finfan: _player.isInsideZone(ZoneId.WATER) ? 1 : _player.isFlyingMounted() ? 2 : 0
			writeC(stateComponent.isRunning() ? 0x01 : 0x00);
		}

		if (containsMask(UserInfoType.COLOR)) {
			writeH(10);
			writeD(player.getNameColor());
			writeD(player.getTitleColor());
		}

		if (containsMask(UserInfoType.INVENTORY_LIMIT)) {
			writeH(9);
			writeH(0x00);
			writeH(0x00);
			writeH(0x00); // _player.getInventoryLimit()
			writeC(0x00); // TODO: cursed weapon equipped
		}

		if (containsMask(UserInfoType.TRUE_HERO)) {
			writeH(9);
			writeD(0x00);
			writeH(0x00);
			writeC(0x00); //_player.isTrueHero() ? 100 : 0x00
		}
		
		/*writeD((int) coordinateComponent.getPosition().getX());
		writeD((int) coordinateComponent.getPosition().getY());
		writeD((int) coordinateComponent.getPosition().getZ());
		writeD((int) coordinateComponent.getPosition().getH());
		writeD(runtimeId);
		writeS(playerComponent.getEntity().getName());
		writeD(playerComponent.getEntity().getRace().ordinal());
		writeD(playerComponent.getEntity().getGender().ordinal());
		writeD(playerComponent.getEntity().getAppearanceClass().getNetworkId(playerComponent.getEntity().getRace()));
		writeD(1); // level
		writeQ(0); //exp
		writeD(1); //str
		writeD(1); // dex
		writeD(1); // con
		writeD(1); // int
		writeD(1); // wit
		writeD(1); // men
		writeD((int) statusComponent.getStatusEntity().getMaxHp());
		writeD((int) statusComponent.getStatusEntity().getHp());
		writeD((int) statusComponent.getStatusEntity().getMaxMp());
		writeD((int) statusComponent.getStatusEntity().getMp());
		writeD(0); // _sp
		writeD(0); // curLoad
		writeD(16500); // maxLoad
		writeD(40); // _pAtkRange

		for (int i = 0; i < 17 * 3; i++) {
			writeD(0x00);
		}
		
		writeD(1); //_patk
		writeD((int) speedComponent.getAttackSpeed()); //_patkspd
		writeD(1); //_pdef
		writeD(1); //evasion
		writeD(1); //accuracy
		writeD(1); //crit
		writeD(1); //_matk
		writeD((int) speedComponent.getCastSpeed()); //_matkspd
		writeD((int) speedComponent.getAttackSpeed()); //_patkspd
		writeD(1); //_mdef
		writeD(playerComponent.getEntity().getPvpMode().ordinal());
		writeD(0); //karma
		writeD((int) speedComponent.getClientRunSpeed());
		writeD((int) speedComponent.getClientWalkSpeed());
		writeD((int) speedComponent.getClientSwimSpeed());
		writeD((int) speedComponent.getClientSwimSpeed()); // _swimWalkSpd
		writeD(0); //_flRunSpd
		writeD(0); //_flWalkSpd
		writeD(0); //_flyRunSpd
		writeD(0); //_flyWalkSpd
		writeF(speedComponent.getAnimMoveSpeed());
		writeF(speedComponent.getAnimAttackSpeed());
		writeF(collisionComponent.getWidth());
		writeF(collisionComponent.getHeight());
		writeD(playerComponent.getEntity().getHairType());
		writeD(playerComponent.getEntity().getHairColor());
		writeD(playerComponent.getEntity().getFaceType());
		writeD(0x00); // access level
		writeS(playerComponent.getEntity().getTitle());
		writeD(0x00); //clan_id
		writeD(clanComponent.getEntity().getCrestId());
		writeD(0x00); //ally_id
		writeD(0x00); //ally_crest_id
		writeD(0x00); //_relation
		writeC(mountComponent.getMountType());
		writeC(storeComponent.getStoreType().getId());
		writeC(0x00); //can_crystalize
		writeD(0x00); //pk_kills
		writeD(0x00); //pvp_kills
		writeH(cubicComponent.getCubics().size());
		for(Integer cubic : cubicComponent.getCubics()) {
			writeH(cubic);
		}
		writeC(stateComponent.isSearchParty() ? 0x01 : 0x00);
		writeD(abnormalComponent.getMask());
		writeC(0x00); //isFlying ? 0x02 : 0x00
		writeD(0x00);//ClanPrivs
		writeH(recommendationComponent.getLeft());
		writeH(recommendationComponent.getCollect());
		writeD(mountComponent.getMountId());
		writeH(100); //InventoryLimit
		writeD(classComponent.getClassId().ordinal());
		writeD(0x00); // special effects? circles around player...
		writeD(1); //maxCp
		writeD(1); //curCp
		writeC(0x00); //encahnt
		writeC(0x00); // team
		writeD(clanComponent.getEntity().getLargeCrestId());
		writeC(stateComponent.isNoble() ? 0x01 : 0x00);
		writeC(stateComponent.isHeroAura() ? 0x01 : 0x00);
		writeC(0x00); //TODO is fishining
		writeD(0x00); //fish loc x
		writeD(0x00); //fish loc y
		writeD(0x00); //fish loc z
		writeD(playerComponent.getEntity().getNameColor());
		writeC(stateComponent.isRunning() ? 0x01 : 0x00);
		writeD(0x00); // pledge class
		writeD(0x00); // pledge type
		writeD(playerComponent.getEntity().getTitleColor());
		writeD(0x00); //cursed weapon level*/
	}

	@Override
	protected byte[] getMasks() {
		return MASKS;
	}
}
