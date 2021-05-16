package sf.l2j.gameserver.network.clientpackets;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import sf.finex.enums.EGradeType;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.ExConfirmVariationRefiner;

/**
 * Fromat(ch) dd
 *
 * @author -Wooden-
 */
public class RequestConfirmRefinerItem extends AbstractRefinePacket {

	private int _targetItemObjId;
	private int _refinerItemObjId;

	@Override
	protected void readImpl() {
		_targetItemObjId = readD();
		_refinerItemObjId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_targetItemObjId);
		if (targetItem == null) {
			return;
		}

		final ItemInstance refinerItem = activeChar.getInventory().getItemByObjectId(_refinerItemObjId);
		if (refinerItem == null) {
			return;
		}

		if (!isValid(activeChar, targetItem, refinerItem)) {
			activeChar.sendPacket(SystemMessageId.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}

		final int refinerItemId = refinerItem.getItem().getItemId();
		final EGradeType grade = targetItem.getItem().getCrystalType();
		final int gemStoneId = getGemStoneId(grade);
		final int gemStoneCount = getGemStoneCount(grade);

		activeChar.sendPacket(new ExConfirmVariationRefiner(_refinerItemObjId, refinerItemId, gemStoneId, gemStoneCount));
	}
}
