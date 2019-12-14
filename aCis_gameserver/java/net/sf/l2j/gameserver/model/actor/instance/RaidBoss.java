package net.sf.l2j.gameserver.model.actor.instance;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;
import java.util.concurrent.ScheduledFuture;

import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.instancemanager.RaidBossPointsManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager.StatusEnum;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class manages all RaidBoss. In a group mob, there are one master called
 * RaidBoss and several slaves called Minions.
 */
public class RaidBoss extends Monster {

	private StatusEnum _raidStatus;
	private ScheduledFuture<?> _maintenanceTask;

	/**
	 * Constructor of L2RaidBossInstance (use Creature and L2NpcInstance
	 * constructor).
	 * <ul>
	 * <li>Call the Creature constructor to set the _template of the
	 * L2RaidBossInstance (copy skills from template to object and link
	 * _calculators to NPC_STD_CALCULATOR)</li>
	 * <li>Set the name of the L2RaidBossInstance</li>
	 * <li>Create a RandomAnimation Task that will be launched after the
	 * calculated delay if the server allow it</li>
	 * </ul>
	 *
	 * @param objectId Identifier of the object to initialized
	 * @param template L2NpcTemplate to apply to the NPC
	 */
	public RaidBoss(int objectId, NpcTemplate template) {
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

		if (_maintenanceTask != null) {
			_maintenanceTask.cancel(false);
			_maintenanceTask = null;
		}

		if (killer != null) {
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
		}

		RaidBossSpawnManager.getInstance().updateStatus(this, true);
		return true;
	}

	@Override
	public void deleteMe() {
		if (_maintenanceTask != null) {
			_maintenanceTask.cancel(false);
			_maintenanceTask = null;
		}

		super.deleteMe();
	}

	/**
	 * Spawn minions.<br>
	 * Also if boss is too far from home location at the time of this check,
	 * teleport it to home.
	 */
	@Override
	protected void startMaintenanceTask() {
		super.startMaintenanceTask();

		_maintenanceTask = ThreadPool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// If the boss is dead, movement disabled, is Gordon or is in combat, return.
				if (isDead() || isMovementDisabled() || getNpcId() == 29095 || isInCombat()) {
					return;
				}

				// Spawn must exist.
				final L2Spawn spawn = getSpawn();
				if (spawn == null) {
					return;
				}

				// If the boss is above drift range (or 200 minimum), teleport him on his spawn.
				if (!isInsideRadius(spawn.getLocX(), spawn.getLocY(), spawn.getLocZ(), Math.max(Config.MAX_DRIFT_RANGE, 200), true, false)) {
					teleToLocation(spawn.getLoc(), 0);
				}
			}
		}, 60000, 30000);
	}

	public StatusEnum getRaidStatus() {
		return _raidStatus;
	}

	public void setRaidStatus(StatusEnum status) {
		_raidStatus = status;
	}
}
