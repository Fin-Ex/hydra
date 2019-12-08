package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.ItemRequest;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.finex.enums.EStoreType;
import net.sf.l2j.gameserver.model.tradelist.TradeList;
import net.sf.l2j.gameserver.network.SystemMessageId;

public final class RequestPrivateStoreSell extends L2GameClientPacket
{
	private static final int BATCH_LENGTH = 20; // length of one item
	
	private int _storePlayerId;
	private ItemRequest[] _items = null;
	
	@Override
	protected void readImpl()
	{
		_storePlayerId = readD();
		int count = readD();
		if (count <= 0 || count > Config.MAX_ITEM_IN_PACKET || count * BATCH_LENGTH != _buf.remaining())
			return;
		
		_items = new ItemRequest[count];
		
		for (int i = 0; i < count; i++)
		{
			int objectId = readD();
			int itemId = readD();
			readH(); // TODO analyse this
			readH(); // TODO analyse this
			long cnt = readD();
			int price = readD();
			
			if (objectId < 1 || itemId < 1 || cnt < 1 || price < 0)
			{
				_items = null;
				return;
			}
			_items[i] = new ItemRequest(objectId, itemId, (int) cnt, price);
		}
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;
		
		if (_items == null)
			return;
		
		Player storePlayer = World.getInstance().getPlayer(_storePlayerId);
		if (storePlayer == null)
			return;
		
		if (!player.isInsideRadius(storePlayer, 150, true, false))
			return;
		
		if (storePlayer.getStoreType() != EStoreType.BUY)
			return;
		
		if (player.isCursedWeaponEquipped())
			return;
		
		TradeList storeList = storePlayer.getBuyList();
		if (storeList == null)
			return;
		
		if (!player.getAccessLevel().allowTransaction())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		if (!storeList.privateStoreSell(player, _items))
		{
			_log.warn("PrivateStore sell has failed due to invalid list or request. Player: " + player.getName() + ", Private store of: " + storePlayer.getName());
			return;
		}
		
		if (storeList.getItems().isEmpty())
		{
			storePlayer.setStoreType(EStoreType.NONE);
			storePlayer.broadcastUserInfo();
		}
	}
}