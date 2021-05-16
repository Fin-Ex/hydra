package sf.l2j.gameserver.model.itemcontainer;

import sf.l2j.Config;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.EItemLocation;
import sf.l2j.gameserver.model.pledge.Clan;

public final class ClanWarehouse extends ItemContainer {

	private final Clan _clan;

	public ClanWarehouse(Clan clan) {
		_clan = clan;
	}

	@Override
	public String getName() {
		return "ClanWarehouse";
	}

	@Override
	public int getOwnerId() {
		return _clan.getClanId();
	}

	@Override
	public Player getOwner() {
		return _clan.getLeader().getPlayerInstance();
	}

	@Override
	public EItemLocation getBaseLocation() {
		return EItemLocation.CLANWH;
	}

	@Override
	public boolean validateCapacity(int slots) {
		return _items.size() + slots <= Config.WAREHOUSE_SLOTS_CLAN;
	}
}