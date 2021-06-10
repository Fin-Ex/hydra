package ru.finex.gs.proto.interlude.outcome;

import lombok.Data;
import ru.finex.gs.model.dto.LobbyAvatarDto;
import ru.finex.gs.proto.network.L2GameServerPacket;
import ru.finex.gs.proto.network.Opcode;
import ru.finex.gs.proto.network.OutcomePacket;
import sf.l2j.gameserver.model.base.ClassId;

import java.util.List;

/**
 * FIXME m0nster.mind: для данного пакета необходимо отдельно создавать GameObject со своим набором компонентов,
 *  который будет отображать аватара игрока в лобби.
 * @author m0nster.mind
 */
@Data
@OutcomePacket(@Opcode(0x13))
public class CharSelectInfo extends L2GameServerPacket {

	private String login;
	private int sessionId;
	private List<LobbyAvatarDto> avatars;

	@Override
	protected final void writeImpl() {
		writeC(0x13);
		writeD(avatars.size());
		for (LobbyAvatarDto avatar : avatars) {
			writeS(avatar.getPlayer().getName());
			writeD(avatar.getPersistenceId()); // obj id
			writeS(login);
			writeD(sessionId);
			writeD(avatar.getClan().getPersistenceId());
			writeD(0x00); // access level
			writeD(avatar.getPlayer().getSex().ordinal());
			writeD(avatar.getPlayer().getRace().ordinal());
			writeD(avatar.getPlayer().getAppearanceClass().getNetworkId(avatar.getPlayer().getRace()));
			writeD(0x01); // server id

			writeD((int) avatar.getPosition().getX());
			writeD((int) avatar.getPosition().getY());
			writeD((int) avatar.getPosition().getZ());

			writeF(avatar.getStatus().getHp());
			writeF(avatar.getStatus().getMp());

			writeD(0); // sp
			writeQ(0); // exp
			writeD(1); // level

			writeD(0); // karma
			writeD(0); // pk count

			writeD(0); // pvp count
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);

			/*
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HAIRALL));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
			writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_FACE));

			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HAIRALL));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_REAR));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_NECK));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_FEET));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_BACK));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
			writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_FACE));
			 */
			for (int i = 0; i < 16 * 2; i++) {
				writeD(0);
			}

			writeD(avatar.getPlayer().getHairType());
			writeD(avatar.getPlayer().getHairColor());
			writeD(avatar.getPlayer().getFaceType());

			writeF(avatar.getStatus().getMaxHp());
			writeF(avatar.getStatus().getMaxMp());

			writeD(-1); // secs to delete avatar
			writeD(ClassId.HumanFighter.ordinal());
			writeD(0x01); // selected avatar or not
			writeC(0); // weapon enchant
			writeD(0); // weapon augmentation
		}
	}

}
