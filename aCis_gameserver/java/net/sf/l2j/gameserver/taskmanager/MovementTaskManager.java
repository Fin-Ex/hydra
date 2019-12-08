package net.sf.l2j.gameserver.taskmanager;

import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.slf4j.Logger;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.actor.ai.type.CreatureAI;

/**
 * Updates position of moving {@link Creature} periodically. Task created as separate Thread with MAX_PRIORITY.
 * @author Forsaiken, Hasha
 */
public final class MovementTaskManager extends Thread
{
	protected static final Logger _log = LoggerFactory.getLogger(MovementTaskManager.class.getName());
	
	// Update the position of all moving characters each MILLIS_PER_UPDATE.
	private static final int MILLIS_PER_UPDATE = 100;
	
	private final Map<Integer, Creature> _characters = new ConcurrentHashMap<>();
	
	public static final MovementTaskManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected MovementTaskManager()
	{
		super("MovementTaskManager");
		super.setDaemon(true);
		super.setPriority(MAX_PRIORITY);
		super.start();
	}
	
	/**
	 * Add a {@link Creature} to MovementTask in order to update its location every MILLIS_PER_UPDATE ms.
	 * @param cha The Creature to add to movingObjects of GameTimeController
	 */
	public final void add(final Creature cha)
	{
		_characters.putIfAbsent(cha.getObjectId(), cha);
	}
	
	@Override
	public final void run()
	{
		_log.info("MovementTaskManager: Started.");
		
		long time = System.currentTimeMillis();
		
		while (true)
		{
			// set next check time
			time += MILLIS_PER_UPDATE;
			
			try
			{
				// For all moving characters.
				for (Iterator<Map.Entry<Integer, Creature>> iterator = _characters.entrySet().iterator(); iterator.hasNext();)
				{
					// Get entry of current iteration.
					Map.Entry<Integer, Creature> entry = iterator.next();
					
					// Get character.
					Creature character = entry.getValue();
					
					// Update character position, final position isn't reached yet.
					if (!character.updatePosition())
						continue;
					
					// Destination reached, remove from map.
					iterator.remove();
					
					// Get character AI, if AI doesn't exist, skip.
					final CreatureAI ai = character.getAI();
					if (ai == null)
						continue;
					
					// Inform AI about arrival.
					ThreadPool.execute(new Runnable()
					{
						@Override
						public final void run()
						{
							try
							{
								ai.notifyEvent(CtrlEvent.EVT_ARRIVED);
							}
							catch (final Throwable e)
							{
								_log.warn( "", e);
							}
						}
					});
				}
			}
			catch (final Throwable e)
			{
				_log.warn( "", e);
			}
			
			// Sleep thread till next tick.
			long sleepTime = time - System.currentTimeMillis();
			if (sleepTime > 0)
			{
				try
				{
					Thread.sleep(sleepTime);
				}
				catch (final InterruptedException e)
				{
					
				}
			}
		}
	}
	
	private static class SingletonHolder
	{
		protected static final MovementTaskManager _instance = new MovementTaskManager();
	}
}