package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.World;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.model.actor.Pet;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;

public final class RequestPetGetItem extends L2GameClientPacket {

	private int _objectId;

	@Override
	protected void readImpl() {
		_objectId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null || !activeChar.hasPet()) {
			return;
		}

		final WorldObject item = World.getInstance().getObject(_objectId);
		if (item == null) {
			return;
		}

		final Pet pet = (Pet) activeChar.getActiveSummon();
		if (pet.isDead() || pet.isOutOfControl()) {
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		pet.getAI().setIntention(CtrlIntention.PICK_UP, item);
	}
}
