package net.sf.l2j.gameserver.model.zone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.scripting.EventType;
import net.sf.l2j.gameserver.scripting.Quest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for any zone type.
 */
public abstract class L2ZoneType {

	protected static final Logger log = LoggerFactory.getLogger(L2ZoneType.class);
	
	private final int _id;
	protected final Map<Integer, Creature> _characterList = new ConcurrentHashMap<>();

	private Map<EventType, List<Quest>> _questEvents;
	private L2ZoneForm form;

	protected L2ZoneType(int id) {
		_id = id;
	}

	protected abstract void onEnter(Creature character);

	protected abstract void onExit(Creature character);

	public abstract void onDieInside(Creature character);

	public abstract void onReviveInside(Creature character);
	
	public void onDestroy() {
		log.info("Destroying: {}", this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + _id + "]";
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return _id;
	}

	/**
	 * @return this zone form.
	 */
	public L2ZoneForm getForm() {
		return form;
	}

	/**
	 * Set the zone for this L2ZoneType Instance
	 *
	 * @param form
	 */
	public void setForm(L2ZoneForm form) {
		if (this.form != null) {
			throw new IllegalStateException("Zone already set");
		}

		this.form = form;
	}

	/**
	 * @param x
	 * @param y
	 * @return true if the given coordinates are within zone's plane
	 */
	public boolean isInsideZone(int x, int y) {
		return form.isInsideZone(x, y, form.getHighZ());
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return true if the given coordinates are within the zone
	 */
	public boolean isInsideZone(int x, int y, int z) {
		return form.isInsideZone(x, y, z);
	}

	/**
	 * @param object check object's X/Y positions.
	 * @return true if the given object is inside the zone.
	 */
	public boolean isInsideZone(WorldObject object) {
		return isInsideZone(object.getX(), object.getY(), object.getZ());
	}

	public double getDistanceToZone(int x, int y) {
		return form.getDistanceToZone(x, y);
	}

	public double getDistanceToZone(WorldObject object) {
		return form.getDistanceToZone(object.getX(), object.getY());
	}

	public void revalidateInZone(Creature character) {
		// If the character can't be affected by this zone return
		if (!isAffected(character)) {
			return;
		}

		// If the object is inside the zone...
		if (isInsideZone(character)) {
			// Was the character not yet inside this zone?
			if (!_characterList.containsKey(character.getObjectId())) {
				// Notify to scripts.
				final List<Quest> quests = getQuestByEvent(EventType.ON_ENTER_ZONE);
				if (quests != null) {
					for (Quest quest : quests) {
						quest.notifyEnterZone(character, this);
					}
				}

				// Register player.
				_characterList.put(character.getObjectId(), character);

				// Notify Zone implementation.
				onEnter(character);
			}
		} else {
			removeCharacter(character);
		}
	}

	/**
	 * Removes a character from the zone.
	 *
	 * @param character : The character to remove.
	 */
	public void removeCharacter(Creature character) {
		// Was the character inside this zone?
		if (_characterList.containsKey(character.getObjectId())) {
			// Notify to scripts.
			final List<Quest> quests = getQuestByEvent(EventType.ON_EXIT_ZONE);
			if (quests != null) {
				for (Quest quest : quests) {
					quest.notifyExitZone(character, this);
				}
			}

			// Unregister player.
			_characterList.remove(character.getObjectId());

			// Notify Zone implementation.
			onExit(character);
		}
	}

	/**
	 * @param character The character to test.
	 * @return True if the character is in the zone.
	 */
	public boolean isCharacterInZone(Creature character) {
		return _characterList.containsKey(character.getObjectId());
	}

	public Collection<Creature> getCharactersInside() {
		return _characterList.values();
	}

	/**
	 * @param <A> : The generic type.
	 * @param type : The instance type to filter.
	 * @return a List of filtered type characters within this zone. Generate a
	 * temporary List.
	 */
	@SuppressWarnings("unchecked")
	public final <A> List<A> getKnownTypeInside(Class<A> type) {
		List<A> result = new ArrayList<>();

		for (WorldObject obj : _characterList.values()) {
			if (type.isAssignableFrom(obj.getClass())) {
				result.add((A) obj);
			}
		}
		return result;
	}

	public void addQuestEvent(EventType eventType, Quest quest) {
		if (_questEvents == null) {
			_questEvents = new HashMap<>();
		}

		List<Quest> eventList = _questEvents.get(eventType);
		if (eventList == null) {
			eventList = new ArrayList<>();
			eventList.add(quest);
			_questEvents.put(eventType, eventList);
		} else {
			eventList.remove(quest);
			eventList.add(quest);
		}
	}

	public List<Quest> getQuestByEvent(EventType EventType) {
		return (_questEvents == null) ? null : _questEvents.get(EventType);
	}

	/**
	 * Broadcasts packet to all players inside the zone
	 *
	 * @param packet The packet to use.
	 */
	public void broadcastPacket(L2GameServerPacket packet) {
		for (Creature character : _characterList.values()) {
			if (character instanceof Player) {
				character.sendPacket(packet);
			}
		}
	}

	/**
	 * Setup new parameters for this zone
	 *
	 * @param name parameter name.
	 * @param value new parameter value.
	 */
	public void setParameter(String name, String value) {
		log.info("Unknown parameter - {} in zone: {}", name, getId());
	}

	/**
	 * @param character The character to test.
	 * @return True if the given character is affected by this zone.
	 */
	protected boolean isAffected(Creature character) {
		// Overriden in children classes.
		return true;
	}

	public void visualizeZone(int z) {
		getForm().visualizeZone(_id, z);
	}
}
