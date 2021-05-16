package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.commons.random.Rnd;

import sf.l2j.gameserver.instancemanager.RaidBossPointsManager;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.model.entity.Hero;
import sf.l2j.gameserver.model.group.Party;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.PlaySound;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class manages all Grand Bosses.
 */
public final class GrandBoss extends Monster {

	/**
	 * Constructor for L2GrandBossInstance. This represent all grandbosses.
	 *
	 * @param objectId ID of the instance
	 * @param template L2NpcTemplate of the instance
	 */
	public GrandBoss(int objectId, NpcTemplate template) {
		super(objectId, template);
		setIsRaid(true);
	}

	@Override
	public void onSpawn() {
		setIsNoRndWalk(true);
		super.onSpawn();
	}

	@Override
	public boolean doDie(Creature killer) {
		if (!super.doDie(killer)) {
			return false;
		}

		final Player player = killer.getPlayer();
		if (player != null) {
			broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.RAID_WAS_SUCCESSFUL));
			broadcastPacket(new PlaySound("systemmsg_e.1209"));

			final Party party = player.getParty();
			if (party != null) {
				for (Player member : party.getMembers()) {
					RaidBossPointsManager.getInstance().addPoints(member, getNpcId(), (getLevel() / 2) + Rnd.get(-5, 5));
					if (member.isNoble()) {
						Hero.getInstance().setRBkilled(member.getObjectId(), getNpcId());
					}
				}
			} else {
				RaidBossPointsManager.getInstance().addPoints(player, getNpcId(), (getLevel() / 2) + Rnd.get(-5, 5));
				if (player.isNoble()) {
					Hero.getInstance().setRBkilled(player.getObjectId(), getNpcId());
				}
			}
		}

		return true;
	}

	@Override
	public boolean returnHome(boolean cleanAggro) {
		return false;
	}
}
