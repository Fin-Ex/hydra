package net.sf.l2j.gameserver.model.actor;


import java.util.ArrayList;
import java.util.List;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.gameserver.data.MapRegionTable;
import net.sf.l2j.gameserver.data.MapRegionTable.TeleportType;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.ai.type.CreatureAI;
import net.sf.l2j.gameserver.model.actor.ai.type.VehicleAI;
import net.sf.l2j.gameserver.model.actor.stat.VehicleStat;
import net.sf.l2j.gameserver.model.actor.template.CreatureTemplate;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.model.location.VehicleLocation;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.VehicleDeparture;
import net.sf.l2j.gameserver.network.serverpackets.VehicleInfo;
import net.sf.l2j.gameserver.network.serverpackets.VehicleStarted;
import net.sf.l2j.gameserver.taskmanager.MovementTaskManager;

public class Vehicle extends Creature {

	private Runnable _engine;

	protected final List<Player> _passengers = new ArrayList<>();

	protected int _dockId;
	protected VehicleLocation[] _currentPath;
	protected int _runState;

	public Vehicle(int objectId, CreatureTemplate template) {
		super(objectId, template);
		setAI(new VehicleAI(this));
	}

	@Override
	public boolean isFlying() {
		return true;
	}

	public boolean canBeControlled() {
		return _engine == null;
	}

	public void registerEngine(Runnable r) {
		_engine = r;
	}

	public void runEngine(int delay) {
		if (_engine != null) {
			ThreadPool.schedule(_engine, delay);
		}
	}

	public void executePath(VehicleLocation[] path) {
		_runState = 0;
		_currentPath = path;

		if (_currentPath != null && _currentPath.length > 0) {
			final VehicleLocation point = _currentPath[0];
			if (point.getMoveSpeed() > 0) {
				getStat().setMoveSpeed(point.getMoveSpeed());
			}
			if (point.getRotationSpeed() > 0) {
				getStat().setRotationSpeed(point.getRotationSpeed());
			}

			getAI().setIntention(CtrlIntention.MOVE_TO, point);
			return;
		}
		getAI().setIntention(CtrlIntention.ACTIVE);
	}

	@Override
	public boolean moveToNextRoutePoint() {
		_move = null;

		if (_currentPath != null) {
			_runState++;
			if (_runState < _currentPath.length) {
				final VehicleLocation point = _currentPath[_runState];
				if (!isMovementDisabled()) {
					if (point.getMoveSpeed() == 0) {
						teleToLocation(point, 0);
						_currentPath = null;
					} else {
						if (point.getMoveSpeed() > 0) {
							getStat().setMoveSpeed(point.getMoveSpeed());
						}
						if (point.getRotationSpeed() > 0) {
							getStat().setRotationSpeed(point.getRotationSpeed());
						}

						MoveData m = new MoveData();
						m.disregardingGeodata = false;
						m.onGeodataPathIndex = -1;
						m._xDestination = point.getX();
						m._yDestination = point.getY();
						m._zDestination = point.getZ();
						m._heading = 0;

						final double dx = point.getX() - getX();
						final double dy = point.getY() - getY();
						final double distance = Math.sqrt(dx * dx + dy * dy);
						if (distance > 1) // vertical movement heading check
						{
							setHeading(MathUtil.calculateHeadingFrom(getX(), getY(), point.getX(), point.getY()));
						}

						m._moveStartTime = System.currentTimeMillis();
						_move = m;

						MovementTaskManager.getInstance().add(this);
						broadcastPacket(new VehicleDeparture(this));
						return true;
					}
				}
			} else {
				_currentPath = null;
			}
		}

		runEngine(10);
		return false;
	}

	@Override
	public VehicleStat getStat() {
		return (VehicleStat) super.getStat();
	}

	@Override
	public void initCharStat() {
		setStat(new VehicleStat(this));
	}

	public boolean isInDock() {
		return _dockId > 0;
	}

	public int getDockId() {
		return _dockId;
	}

	public void setInDock(int d) {
		_dockId = d;
	}

	public void oustPlayers() {
		for (Player player : _passengers) {
			oustPlayer(player, false, Location.DUMMY_LOC);
		}

		_passengers.clear();
	}

	public void oustPlayer(Player player, boolean removeFromList, Location location) {
		player.setVehicle(null);

		if (removeFromList) {
			removePassenger(player);
		}

		player.setInsideZone(ZoneId.PEACE, false);
		player.sendPacket(SystemMessageId.EXIT_PEACEFUL_ZONE);

		final Location loc = (location.equals(Location.DUMMY_LOC)) ? MapRegionTable.getInstance().getLocationToTeleport(this, TeleportType.TOWN) : location;
		if (player.isOnline()) {
			player.teleToLocation(loc.getX(), loc.getY(), loc.getZ(), 0);
		} else {
			player.setXYZInvisible(loc); // disconnects handling
		}
	}

	public boolean addPassenger(Player player) {
		if (player == null || _passengers.contains(player)) {
			return false;
		}

		// already in other vehicle
		if (player.getVehicle() != null && player.getVehicle() != this) {
			return false;
		}

		_passengers.add(player);

		player.setInsideZone(ZoneId.PEACE, true);
		player.sendPacket(SystemMessageId.ENTER_PEACEFUL_ZONE);

		return true;
	}

	public void removePassenger(Player player) {
		_passengers.remove(player);
	}

	public boolean isEmpty() {
		return _passengers.isEmpty();
	}

	public List<Player> getPassengers() {
		return _passengers;
	}

	public void broadcastToPassengers(L2GameServerPacket sm) {
		for (Player player : _passengers) {
			if (player != null) {
				player.sendPacket(sm);
			}
		}
	}

	/**
	 * Consume ticket(s) and teleport player from boat if no correct ticket
	 *
	 * @param itemId Ticket itemId
	 * @param count Ticket count
	 * @param loc The location to port player in case he can't pay.
	 */
	public void payForRide(int itemId, int count, Location loc) {
		for (Player player : getKnownTypeInRadius(Player.class, 1000)) {
			if (player.isInBoat() && player.getBoat() == this) {
				if (itemId > 0) {
					if (!player.destroyItemByItemId("Boat", itemId, count, this, false)) {
						oustPlayer(player, true, loc);
						player.sendPacket(SystemMessageId.NOT_CORRECT_BOAT_TICKET);
						continue;
					}

					if (count > 1) {
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED).addItemName(itemId).addItemNumber(count));
					} else {
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DISAPPEARED).addItemName(itemId));
					}
				}
				addPassenger(player);
			}
		}
	}

	@Override
	public boolean updatePosition() {
		final boolean result = super.updatePosition();

		for (Player player : _passengers) {
			if (player != null && player.getVehicle() == this) {
				player.setXYZ(getX(), getY(), getZ());
				player.revalidateZone(false);
			}
		}
		return result;
	}

	@Override
	public Location teleToLocation(int x, int y, int z, int randomOffset) {
		if (isMoving()) {
			stopMove(null);
		}

		setIsTeleporting(true);

		getAI().setIntention(CtrlIntention.ACTIVE);

		for (Player player : _passengers) {
			if (player != null) {
				player.teleToLocation(x, y, z, randomOffset);
			}
		}

		decayMe();
		setXYZ(x, y, z);

		onTeleported();
		revalidateZone(true);
		return new Location(x, y, z);
	}

	@Override
	public void stopMove(SpawnLocation loc) {
		_move = null;

		if (loc != null) {
			setXYZ(loc.getX(), loc.getY(), loc.getZ());
			setHeading(loc.getHeading());
			revalidateZone(true);
		}

		broadcastPacket(new VehicleStarted(this, 0));
		broadcastPacket(new VehicleInfo(this));
	}

	@Override
	public void deleteMe() {
		_engine = null;

		if (isMoving()) {
			stopMove(null);
		}

		// Oust all players.
		oustPlayers();

		// Decay the vehicle.
		decayMe();

		super.deleteMe();
	}

	@Override
	public void updateAbnormalEffect() {
	}

	@Override
	public ItemInstance getActiveWeaponInstance() {
		return null;
	}

	@Override
	public Weapon getActiveWeaponItem() {
		return null;
	}

	@Override
	public ItemInstance getSecondaryWeaponInstance() {
		return null;
	}

	@Override
	public Weapon getSecondaryWeaponItem() {
		return null;
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public boolean isAutoAttackable(Creature attacker) {
		return false;
	}

	@Override
	public void setAI(CreatureAI newAI) {
		if (_ai == null) {
			_ai = newAI;
		}
	}

	@Override
	public void detachAI() {
	}

	@Override
	public void sendInfo(Player activeChar) {
		activeChar.sendPacket(new VehicleInfo(this));
	}

	@Override
	public boolean isVehicle() {
		return true;
	}

	@Override
	public Vehicle getVehicle() {
		return this;
	}
}
