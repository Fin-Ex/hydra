package net.sf.l2j.gameserver.taskmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.events.OnAttackStance;
import net.sf.l2j.gameserver.model.actor.instance.Cubic;
import net.sf.l2j.gameserver.network.serverpackets.AutoAttackStop;

/**
 * Turns off attack stance of {@link Creature} after PERIOD ms.
 *
 * @author Luca Baldi, Hasha
 */
public final class AttackStanceTaskManager implements Runnable {

	private static final long ATTACK_STANCE_PERIOD = 15000; // 15 seconds

	private final Map<Creature, Long> _characters = new ConcurrentHashMap<>();

	public static final AttackStanceTaskManager getInstance() {
		return SingletonHolder._instance;
	}

	protected AttackStanceTaskManager() {
		// Run task each second.
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}

	/**
	 * Adds {@link Creature} to the AttackStanceTask.
	 *
	 * @param character : {@link Creature} to be added and checked.
	 */
	public final void add(Creature character) {
		if (character instanceof Playable) {
			for (Cubic cubic : character.getPlayer().getCubics().values()) {
				if (cubic.getId() != Cubic.LIFE_CUBIC) {
					cubic.doAction();
				}
			}

			character.getEventBus().notify(new OnAttackStance(character, false));
		}

		_characters.put(character, System.currentTimeMillis() + ATTACK_STANCE_PERIOD);
	}

	/**
	 * Removes {@link Creature} from the AttackStanceTask.
	 *
	 * @param character : {@link Creature} to be removed.
	 */
	public final void remove(Creature character) {
		if (character != null) {
			if (character instanceof Summon) {
				character = character.getPlayer();
			}

			character.getEventBus().notify(new OnAttackStance(character, true));
		}

		_characters.remove(character);
	}

	/**
	 * Tests if {@link Creature} is in AttackStanceTask.
	 *
	 * @param character : {@link Creature} to be removed.
	 * @return boolean : True when {@link Creature} is in attack stance.
	 */
	public final boolean isInAttackStance(Creature character) {
		if (character instanceof Summon) {
			character = character.getPlayer();
		}

		return _characters.containsKey(character);
	}

	@Override
	public final void run() {
		// List is empty, skip.
		if (_characters.isEmpty()) {
			return;
		}

		// Get current time.
		final long time = System.currentTimeMillis();

		// Loop all characters.
		for (Map.Entry<Creature, Long> entry : _characters.entrySet()) {
			// Time hasn't passed yet, skip.
			if (time < entry.getValue()) {
				continue;
			}

			// Get character.
			final Creature character = entry.getKey();

			// Stop character attack stance animation.
			character.broadcastPacket(new AutoAttackStop(character.getObjectId()));

			// Stop pet attack stance animation.
			if (character instanceof Player && ((Player) character).getActiveSummon() != null) {
				((Player) character).getActiveSummon().broadcastPacket(new AutoAttackStop(((Player) character).getActiveSummon().getObjectId()));
			}

			// Inform character AI and remove task.
			character.getAI().setAutoAttacking(false);
			_characters.remove(character);
		}
	}

	private static class SingletonHolder {

		protected static final AttackStanceTaskManager _instance = new AttackStanceTaskManager();
	}
}
