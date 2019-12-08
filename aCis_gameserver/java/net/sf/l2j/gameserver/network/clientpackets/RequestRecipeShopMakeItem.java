package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.finex.data.tables.RecipeTable;
import net.sf.finex.model.craft.Craft;
import net.sf.finex.model.craft.Multiple;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.finex.enums.EStoreType;
import net.sf.l2j.gameserver.network.FloodProtectors;
import net.sf.l2j.gameserver.network.FloodProtectors.Action;
import net.sf.l2j.gameserver.network.SystemMessageId;

public final class RequestRecipeShopMakeItem extends L2GameClientPacket {

	private int _id;
	private int _recipeId;
	@SuppressWarnings("unused")
	private int _unknow;

	@Override
	protected void readImpl() {
		_id = readD();
		_recipeId = readD();
		_unknow = readD();
	}

	@Override
	protected void runImpl() {
		if (!FloodProtectors.performAction(getClient(), Action.MANUFACTURE)) {
			return;
		}

		final Player client = getClient().getActiveChar();
		if (client == null) {
			return;
		}

		final Player manufacturer = World.getInstance().getPlayer(_id);
		if (manufacturer == null) {
			return;
		}

		if (client.isInStoreMode()) {
			return;
		}

		if (manufacturer.getStoreType() != EStoreType.MANUFACTURE) {
			return;
		}

		if (client.isCrafting() || manufacturer.isCrafting()) {
			return;
		}

		if (manufacturer.isInDuel() || client.isInDuel()) {
			client.sendPacket(SystemMessageId.CANT_OPERATE_PRIVATE_STORE_DURING_COMBAT);
			return;
		}

		if (MathUtil.checkIfInRange(150, client, manufacturer, true)) {
			final Craft craft = new Multiple(manufacturer, client, RecipeTable.getInstance().get(_recipeId));
			client.setCraft(craft);
			craft.create();
		}
	}
}
