package ru.finex.ws.l2.network.outcome;

import lombok.Data;
import ru.finex.ws.l2.component.base.CoordinateComponent;
import ru.finex.ws.l2.component.base.StatusComponent;
import ru.finex.ws.l2.component.player.AbnormalComponent;
import ru.finex.ws.l2.component.player.ClanComponent;
import ru.finex.ws.l2.component.player.ClassComponent;
import ru.finex.ws.l2.component.player.CollisionComponent;
import ru.finex.ws.l2.component.player.CubicComponent;
import ru.finex.ws.l2.component.player.MountComponent;
import ru.finex.ws.l2.component.player.PlayerComponent;
import ru.finex.ws.l2.component.player.RecommendationComponent;
import ru.finex.ws.l2.component.player.SpeedComponent;
import ru.finex.ws.l2.component.player.StateComponent;
import ru.finex.ws.l2.component.player.StoreComponent;
import ru.finex.ws.l2.network.Opcode;
import ru.finex.ws.l2.network.OutcomePacket;
import ru.finex.ws.l2.network.model.L2GameServerPacket;

/**
 * @author finfan
 */
@Data
@OutcomePacket(@Opcode(0x04))
public class UserInfo extends L2GameServerPacket {
	
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
	
	@Override
	protected void writeImpl() {
		writeC(0x04);
		writeD((int) coordinateComponent.getPosition().getX());
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
		
		/*
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIRALL));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
		body.writeD(_activeChar.getInventory().getPaperdollVisualObjectId(Inventory.PAPERDOLL_HEAD, _visual_obj_id));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
		body.writeD(_activeChar.getInventory().getPaperdollVisualObjectId(Inventory.PAPERDOLL_GLOVES, _visual_obj_id));
		body.writeD(_activeChar.getInventory().getPaperdollVisualObjectId(Inventory.PAPERDOLL_CHEST, _visual_obj_id));
		body.writeD(_activeChar.getInventory().getPaperdollVisualObjectId(Inventory.PAPERDOLL_LEGS, _visual_obj_id));
		body.writeD(_activeChar.getInventory().getPaperdollVisualObjectId(Inventory.PAPERDOLL_FEET, _visual_obj_id));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
		body.writeD(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FACE));


		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HAIRALL));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_REAR));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LEAR));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_NECK));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RFINGER));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LFINGER));
		body.writeD(_activeChar.getInventory().getPaperdollVisualItemId(Inventory.PAPERDOLL_HEAD, _visual));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RHAND));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_LHAND));
		body.writeD(_activeChar.getInventory().getPaperdollVisualItemId(Inventory.PAPERDOLL_GLOVES, _visual));
		body.writeD(_activeChar.getInventory().getPaperdollVisualItemId(Inventory.PAPERDOLL_CHEST, _visual));
		body.writeD(_activeChar.getInventory().getPaperdollVisualItemId(Inventory.PAPERDOLL_LEGS, _visual));
		body.writeD(_activeChar.getInventory().getPaperdollVisualItemId(Inventory.PAPERDOLL_FEET, _visual));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_BACK));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_RHAND));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_HAIR));
		body.writeD(_activeChar.getInventory().getPaperdollItemDisplayId(Inventory.PAPERDOLL_FACE));

		for(int slot : Inventory.PAPERDOLL_ORDER)
			body.writeD(_activeChar.getInventory().getPaperdollAugmentationId(slot));
		 */
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
		writeC(mountComponent.getMountType().ordinal());
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
		writeD(0x00); //cursed weapon level
	}
}
