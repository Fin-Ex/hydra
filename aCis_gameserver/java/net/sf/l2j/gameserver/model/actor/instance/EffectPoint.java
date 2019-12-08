package net.sf.l2j.gameserver.model.actor.instance;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;

public class EffectPoint extends Npc
{
	private final Player _owner;
	
	public EffectPoint(int objectId, NpcTemplate template, Creature owner)
	{
		super(objectId, template);
		_owner = owner == null ? null : owner.getPlayer();
	}
	
	@Override
	public Player getPlayer()
	{
		return _owner;
	}
	
	@Override
	public void onAction(Player player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	@Override
	public void onActionShift(Player player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}