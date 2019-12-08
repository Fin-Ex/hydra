package net.sf.l2j.gameserver.model.actor.status;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import net.sf.finex.model.regeneration.ERegenType;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatureStatus
{
	protected static final Logger _log = LoggerFactory.getLogger(CreatureStatus.class.getName());
	
	private final Creature _activeChar;
	
	private final Set<Creature> _statusListener = ConcurrentHashMap.newKeySet();
	
	private double _currentHp = 0;
	private double _currentMp = 0;
	
	public CreatureStatus(Creature activeChar)
	{
		_activeChar = activeChar;
	}
	
	/**
	 * Add the object to the list of Creature that must be informed of HP/MP updates of this Creature.
	 * @param object : Creature to add to the listener.
	 */
	public final void addStatusListener(Creature object)
	{
		if (object == getActiveChar())
			return;
		
		_statusListener.add(object);
	}
	
	/**
	 * Remove the object from the list of Creature that must be informed of HP/MP updates of this Creature.
	 * @param object : Creature to remove to the listener.
	 */
	public final void removeStatusListener(Creature object)
	{
		_statusListener.remove(object);
	}
	
	/**
	 * @return The list of Creature to inform, or null if empty.
	 */
	public final Set<Creature> getStatusListener()
	{
		return _statusListener;
	}
	
	public void reduceCp(int value)
	{
	}
	
	/**
	 * Reduce the current HP of the Creature and launch the doDie Task if necessary.
	 * @param value : The amount of removed HPs.
	 * @param attacker : The Creature who attacks.
	 */
	public void reduceHp(double value, Creature attacker)
	{
		reduceHp(value, attacker, true, false, false);
	}
	
	public void reduceHp(double value, Creature attacker, boolean isHpConsumption)
	{
		reduceHp(value, attacker, true, false, isHpConsumption);
	}
	
	public void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHPConsumption)
	{
		if (getActiveChar().isDead())
			return;
		
		// invul handling
		if (getActiveChar().isInvul())
		{
			// other chars can't damage
			if (attacker != getActiveChar())
				return;
			
			// only DOT and HP consumption allowed for damage self
			if (!isDOT && !isHPConsumption)
				return;
		}
		
		if (attacker != null)
		{
			final Player attackerPlayer = attacker.getPlayer();
			if (attackerPlayer != null && attackerPlayer.isGM() && !attackerPlayer.getAccessLevel().canGiveDamage())
				return;
		}
		
		if (!isDOT && !isHPConsumption)
		{
			getActiveChar().stopEffectsOnDamage(awake);
			
			if (getActiveChar().isStunned() && 12 > Rnd.get(100))
				getActiveChar().stopStunning(true);
			
			if (getActiveChar().isAfraid() && 12 > Rnd.get(100))
				getActiveChar().stopFear(true);
			
			if (getActiveChar().isImmobileUntilAttacked())
				getActiveChar().stopImmobileUntilAttacked(null);
		}
		
		if (value > 0) // Reduce Hp if any
			setCurrentHp(Math.max(getCurrentHp() - value, 0));
		
		// Die if character is mortal
		if (getActiveChar().getCurrentHp() < 0.5 && getActiveChar().isMortal())
		{
			getActiveChar().abortAttack();
			getActiveChar().abortCast();
			getActiveChar().doDie(attacker);
		}
	}
	
	public void reduceMp(double value)
	{
		setCurrentMp(Math.max(getCurrentMp() - value, 0));
	}
	
	public double getCurrentCp()
	{
		return 0;
	}
	
	public void setCurrentCp(double newCp)
	{
	}
	
	public final double getCurrentHp()
	{
		return _currentHp;
	}
	
	public final void setCurrentHp(double newHp)
	{
		setCurrentHp(newHp, true);
	}
	
	public void setCurrentHp(double newHp, boolean broadcastPacket)
	{
		final double maxHp = getActiveChar().getMaxHp();
		
		synchronized (this)
		{
			if (getActiveChar().isDead())
				return;
			
			if (newHp >= maxHp)
			{
				// Set the RegenActive flag to false
				_currentHp = maxHp;
				stopRegen(ERegenType.HP);
			}
			else
			{
				// Set the RegenActive flag to true
				_currentHp = newHp;
				startRegen(ERegenType.HP);
			}
		}
		
		if (broadcastPacket)
			getActiveChar().broadcastStatusUpdate();
	}
	
	public final void setCurrentHpMp(double newHp, double newMp)
	{
		setCurrentHp(newHp, false);
		setCurrentMp(newMp, true);
	}
	
	public final double getCurrentMp()
	{
		return _currentMp;
	}
	
	public final void setCurrentMp(double newMp)
	{
		setCurrentMp(newMp, true);
	}
	
	public final void setCurrentMp(double newMp, boolean broadcastPacket)
	{
		final int maxMp = getActiveChar().getStat().getMaxMp();
		
		synchronized (this)
		{
			if (getActiveChar().isDead())
				return;
			
			if (newMp >= maxMp)
			{
				_currentMp = maxMp;
				stopRegen(ERegenType.MP);
			}
			else
			{
				_currentMp = newMp;
				startRegen(ERegenType.MP);
			}
		}
		
		if (broadcastPacket)
			getActiveChar().broadcastStatusUpdate();
	}
	
	/**
	 * REGENERATION
	 */
	
	protected Future<?> hpTask, mpTask;
	public void startRegen(ERegenType... regens) {
		for (ERegenType regenType : regens) {
			final int interval = interval(regenType);
			final net.sf.finex.model.regeneration.RegenTask task = new net.sf.finex.model.regeneration.RegenTask(_activeChar, regenType);
			switch (regenType) {
				case HP:
					if (hpTask == null) {
						hpTask = ThreadPool.scheduleAtFixedRate(task, interval, interval);
					}
					break;

				case MP:
					if (mpTask == null) {
						mpTask = ThreadPool.scheduleAtFixedRate(task, interval, interval);
					}
					break;
			}
		}
	}

	public void stopRegen(ERegenType... regens) {
		for (ERegenType regenType : regens) {
			switch (regenType) {
				case HP:
					if (hpTask != null) {
						hpTask.cancel(false);
						hpTask = null;
					}
					break;

				case MP:
					if (mpTask != null) {
						mpTask.cancel(false);
						mpTask = null;
					}
					break;
			}
		}
	}

	public int interval(ERegenType type) {
		switch (type) {
			case HP:
				return _activeChar.getStat().getHpRegenInterval();

			case MP:
				return _activeChar.getStat().getMpRegenInterval();
		}

		return 3000;
	}

	public Creature getActiveChar()
	{
		return _activeChar;
	}
	
}