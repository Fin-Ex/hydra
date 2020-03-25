package net.sf.l2j.gameserver.model.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.data.TalentBranchData;
import net.sf.finex.data.TalentData;
import net.sf.finex.data.tables.TalentBranchTable;
import net.sf.finex.data.tables.TalentTable;
import net.sf.finex.enums.ESkillAlignmentType;
import net.sf.finex.events.EventBus;
import net.sf.finex.model.casting.Cast;
import net.sf.finex.model.creature.attack.AbstractHit;
import net.sf.finex.model.generator.quest.RandomQuestComponent;
import net.sf.finex.model.regeneration.ERegenType;
import net.sf.l2j.Config;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.MapRegionTable;
import net.sf.l2j.gameserver.data.MapRegionTable.TeleportType;
import net.sf.l2j.gameserver.data.SkillTable.FrequentSkill;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.model.ChanceSkillList;
import net.sf.l2j.gameserver.model.CharEffectList;
import net.sf.l2j.gameserver.model.FusionSkill;
import net.sf.l2j.gameserver.model.IChanceSkillTrigger;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.WorldRegion;
import net.sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.ai.NotifyAITask;
import net.sf.l2j.gameserver.model.actor.ai.type.AttackableAI;
import net.sf.l2j.gameserver.model.actor.ai.type.CreatureAI;
import net.sf.l2j.gameserver.model.actor.events.OnKill;
import net.sf.l2j.gameserver.model.actor.events.OnReduceHp;
import net.sf.l2j.gameserver.model.actor.instance.RiftInvader;
import net.sf.l2j.gameserver.model.actor.instance.Walker;
import net.sf.l2j.gameserver.model.actor.stat.CreatureStat;
import net.sf.l2j.gameserver.model.actor.status.CreatureStatus;
import net.sf.l2j.gameserver.model.actor.template.CreatureTemplate;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.model.item.type.WeaponType;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.AbstractNpcInfo.NpcInfo;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ChangeMoveType;
import net.sf.l2j.gameserver.network.serverpackets.ChangeWaitType;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillCanceld;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.MoveToLocation;
import net.sf.l2j.gameserver.network.serverpackets.Revive;
import net.sf.l2j.gameserver.network.serverpackets.ServerObjectInfo;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.StopMove;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.TeleportToLocation;
import net.sf.l2j.gameserver.scripting.EventType;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.skills.AbnormalEffect;
import net.sf.l2j.gameserver.skills.Calculator;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.skills.basefuncs.Func;
import net.sf.l2j.gameserver.skills.effects.EffectChanceSkillTrigger;
import net.sf.l2j.gameserver.skills.funcs.FuncAtkAccuracy;
import net.sf.l2j.gameserver.skills.funcs.FuncAtkCritical;
import net.sf.l2j.gameserver.skills.funcs.FuncAtkEvasion;
import net.sf.l2j.gameserver.skills.funcs.FuncMAtkCritical;
import net.sf.l2j.gameserver.skills.funcs.FuncMAtkMod;
import net.sf.l2j.gameserver.skills.funcs.FuncMAtkSpeed;
import net.sf.l2j.gameserver.skills.funcs.FuncMDefMod;
import net.sf.l2j.gameserver.skills.funcs.FuncMaxHpMul;
import net.sf.l2j.gameserver.skills.funcs.FuncMaxMpMul;
import net.sf.l2j.gameserver.skills.funcs.FuncMoveSpeed;
import net.sf.l2j.gameserver.skills.funcs.FuncPAtkMod;
import net.sf.l2j.gameserver.skills.funcs.FuncPAtkSpeed;
import net.sf.l2j.gameserver.skills.funcs.FuncPDefMod;
import net.sf.l2j.gameserver.taskmanager.AttackStanceTaskManager;
import net.sf.l2j.gameserver.taskmanager.MovementTaskManager;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;
import net.sf.l2j.gameserver.templates.skills.ESkillType;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;
import net.sf.l2j.gameserver.util.Broadcast;

/**
 * Creature is the mother class of all character objects of the world (PC,
 * NPC...) :
 * <ul>
 * <li>L2CastleGuardInstance</li>
 * <li>L2DoorInstance</li>
 * <li>L2Npc</li>
 * <li>L2Playable</li>
 * </ul>
 */
@Slf4j
public abstract class Creature extends WorldObject {

	private volatile boolean _isCastingNow = false;
	private volatile boolean _isCastingSimultaneouslyNow = false;
	private L2Skill _lastSkillCast;
	private L2Skill _lastSimultaneousSkillCast;

	private boolean _isImmobilized = false;
	private boolean _isOverloaded = false;
	private boolean _isParalyzed = false;
	private boolean _isDead = false;
	private boolean _isRunning = false;
	protected boolean _isTeleporting = false;
	protected boolean _showSummonAnimation = false;

	protected boolean _isInvul = false;
	private boolean _isMortal = true;

	private boolean _isNoRndWalk = false;
	private boolean _AIdisabled = false;

	private CreatureStat _stat;
	private CreatureStatus _status;
	private CreatureTemplate _template; // The link on the L2CharTemplate object containing generic and static properties

	protected String _title;
	private double _hpUpdateIncCheck = .0;
	private double _hpUpdateDecCheck = .0;
	private double _hpUpdateInterval = .0;
	private boolean _champion;

	private final Calculator[] _calculators;

	private ChanceSkillList _chanceSkills;
	protected FusionSkill _fusionSkill;

	/**
	 * Zone system
	 */
	private final byte[] _zones = new byte[ZoneId.VALUES.length];
	protected byte _zoneValidateCounter = 4;

	private boolean _isRaid;

	@Setter private boolean isOutOfControl;
	@Setter private boolean isAttackingDisabled;

	@Getter private final EventBus eventBus = new EventBus();
	@Getter private final StatsSet params = new StatsSet();
	@Getter @Setter private boolean isOnMovie = false;
	
	/**
	 * Constructor of Creature.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * Each Creature owns generic and static properties (ex : all Keltir have
	 * the same number of HP...). All of those properties are stored in a
	 * different template for each type of Creature. Each template is loaded
	 * once in the server cache memory (reduce memory use). When a new instance
	 * of Creature is spawned, server just create a link between the instance
	 * and the template This link is stored in <B>_template</B><BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Set the _template of the Creature</li>
	 * <li>Set _overloaded to false (the charcater can take more items)</li>
	 * </ul>
	 * <ul>
	 * <li>If Creature is a L2Npc, copy skills from template to object</li>
	 * <li>If Creature is a L2Npc, link _calculators to NPC_STD_CALCULATOR</li>
	 * </ul>
	 * <ul>
	 * <li>If Creature is NOT a L2Npc, create an empty _skills slot</li>
	 * <li>If Creature is a Player or L2Summon, copy basic Calculator set to
	 * object</li>
	 * </ul>
	 *
	 * @param objectId Identifier of the object to initialized
	 * @param template The L2CharTemplate to apply to the object
	 */
	public Creature(int objectId, CreatureTemplate template) {
		super(objectId);
		initCharStat();
		initCharStatus();

		// Set its template to the new Creature
		_template = template;

		_calculators = new Calculator[Stats.NUM_STATS];
		addFuncsToNewCharacter();
	}

	/**
	 * This method is overidden in
	 * <ul>
	 * <li>Player</li>
	 * <li>L2DoorInstance</li>
	 * </ul>
	 */
	public void addFuncsToNewCharacter() {
		addStatFunc(FuncPAtkMod.getInstance());
		addStatFunc(FuncMAtkMod.getInstance());
		addStatFunc(FuncPDefMod.getInstance());
		addStatFunc(FuncMDefMod.getInstance());

		addStatFunc(FuncMaxHpMul.getInstance());
		addStatFunc(FuncMaxMpMul.getInstance());

		addStatFunc(FuncAtkAccuracy.getInstance());
		addStatFunc(FuncAtkEvasion.getInstance());

		addStatFunc(FuncPAtkSpeed.getInstance());
		addStatFunc(FuncMAtkSpeed.getInstance());

		addStatFunc(FuncMoveSpeed.getInstance());

		addStatFunc(FuncAtkCritical.getInstance());
		addStatFunc(FuncMAtkCritical.getInstance());
	}

	protected void initCharStatusUpdateValues() {
		_hpUpdateInterval = getMaxHp() / 352.0; // MAX_HP div MAX_HP_BAR_PX
		_hpUpdateIncCheck = getMaxHp();
		_hpUpdateDecCheck = getMaxHp() - _hpUpdateInterval;
	}

	/**
	 * Remove the Creature from the world when the decay task is launched.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T REMOVE the
	 * object from _objects of World.</B></FONT><BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T SEND
	 * Server->Client packets to players.</B></FONT>
	 */
	public void onDecay() {
		decayMe();
	}

	@Override
	public void onSpawn() {
		super.onSpawn();
		revalidateZone(true);
	}

	public void onTeleported() {
		if (!isTeleporting()) {
			return;
		}

		spawnMe();
		setIsTeleporting(false);
	}

	/**
	 * @return character inventory, default null, overridden in L2Playable types
	 * and in L2Npc.
	 */
	public Inventory getInventory() {
		return null;
	}

	public boolean destroyItemByItemId(String process, int itemId, int count, WorldObject reference, boolean sendMessage) {
		return true;
	}

	public boolean destroyItem(String process, int objectId, int count, WorldObject reference, boolean sendMessage) {
		return true;
	}

	@Override
	public boolean isInsideZone(ZoneId zone) {
		return zone == ZoneId.PVP ? _zones[ZoneId.PVP.getId()] > 0 && _zones[ZoneId.PEACE.getId()] == 0 : _zones[zone.getId()] > 0;
	}

	public void setInsideZone(ZoneId zone, boolean state) {
		if (state) {
			_zones[zone.getId()]++;
		} else {
			_zones[zone.getId()]--;
			if (_zones[zone.getId()] < 0) {
				_zones[zone.getId()] = 0;
			}
		}
	}

	/**
	 * @return true if the player is GM.
	 */
	public boolean isGM() {
		return false;
	}

	/**
	 * Send a packet to the Creature AND to all Player in the _KnownPlayers of
	 * the Creature.
	 *
	 * @param mov The packet to send.
	 */
	public void broadcastPacket(L2GameServerPacket mov) {
		Broadcast.toSelfAndKnownPlayers(this, mov);
	}

	/**
	 * Send a packet to the Creature AND to all Player in the radius (max
	 * knownlist radius) from the Creature.
	 *
	 * @param mov The packet to send.
	 * @param radius The radius to make check on.
	 */
	public void broadcastPacket(L2GameServerPacket mov, int radius) {
		Broadcast.toSelfAndKnownPlayersInRadius(this, mov, radius);
	}

	/**
	 * @param barPixels
	 * @return boolean true if hp update should be done, false if not.
	 */
	protected boolean needHpUpdate(int barPixels) {
		double currentHp = getCurrentHp();

		if (currentHp <= 1.0 || getMaxHp() < barPixels) {
			return true;
		}

		if (currentHp <= _hpUpdateDecCheck || currentHp >= _hpUpdateIncCheck) {
			if (currentHp == getMaxHp()) {
				_hpUpdateIncCheck = currentHp + 1;
				_hpUpdateDecCheck = currentHp - _hpUpdateInterval;
			} else {
				double doubleMulti = currentHp / _hpUpdateInterval;
				int intMulti = (int) doubleMulti;

				_hpUpdateDecCheck = _hpUpdateInterval * (doubleMulti < intMulti ? intMulti-- : intMulti);
				_hpUpdateIncCheck = _hpUpdateDecCheck + _hpUpdateInterval;
			}
			return true;
		}
		return false;
	}

	/**
	 * Send the Server->Client packet StatusUpdate with current HP and MP to all
	 * other Player to inform.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Create the Server->Client packet StatusUpdate with current HP and
	 * MP</li>
	 * <li>Send the Server->Client packet StatusUpdate with current HP and MP to
	 * all Creature called _statusListener that must be informed of HP/MP
	 * updates of this Creature</li>
	 * </ul>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T SEND CP
	 * information</B></FONT><BR>
	 * <BR>
	 * <B><U>Overriden in Player</U></B> : Send current HP,MP and CP to the
	 * Player and only current HP, MP and Level to all other Player of the Party
	 */
	public void broadcastStatusUpdate() {
		if (getStatus().getStatusListener().isEmpty()) {
			return;
		}

		if (!needHpUpdate(352)) {
			return;
		}

		// Create the Server->Client packet StatusUpdate with current HP
		StatusUpdate su = new StatusUpdate(this);
		su.addAttribute(StatusUpdate.CUR_HP, (int) getCurrentHp());

		// Go through the StatusListener
		for (Creature temp : getStatus().getStatusListener()) {
			if (temp != null) {
				temp.sendPacket(su);
			}
		}
	}

	/**
	 * <B><U> Overriden in </U> :</B><BR>
	 * <BR>
	 * <li>Player</li><BR>
	 * <BR>
	 *
	 * @param mov The packet to send.
	 */
	public void sendPacket(L2GameServerPacket mov) {
		// default implementation
	}

	/**
	 * <B><U> Overridden in </U> :</B><BR>
	 * <BR>
	 * <li>Player</li><BR>
	 * <BR>
	 *
	 * @param text The string to send.
	 */
	public void sendMessage(String text) {
		// default implementation
	}

	/**
	 * Teleport a Creature and its pet if necessary.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Stop the movement of the Creature</li>
	 * <li>Set the x,y,z position of the Creature and if necessary modify its
	 * _worldRegion</li>
	 * <li>Send TeleportToLocationt to the Creature AND to all Player in its
	 * _KnownPlayers</li>
	 * <li>Modify the position of the pet if necessary</li>
	 * </ul>
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param randomOffset
	 * @return new location where teleport
	 */
	public Location teleToLocation(int x, int y, int z, int randomOffset) {
		// Stop movement
		stopMove(null);
		abortAttack();
		abortCast();

		setIsTeleporting(true);
		setTarget(null);

		getAI().setIntention(CtrlIntention.ACTIVE);

		if (randomOffset > 0) {
			x += Rnd.get(-randomOffset, randomOffset);
			y += Rnd.get(-randomOffset, randomOffset);
		}

		z += 5;

		// Send TeleportToLocationt to the Creature AND to all Player in the _KnownPlayers of the Creature
		broadcastPacket(new TeleportToLocation(this, x, y, z));

		// remove the object from its old location
		decayMe();

		// Set the x,y,z position of the WorldObject and if necessary modify its _worldRegion
		setXYZ(x, y, z);

		if (!(this instanceof Player) || (((Player) this).getClient() != null && ((Player) this).getClient().isDetached())) {
			onTeleported();
		}

		revalidateZone(true);
		return new Location(x,y,z);
	}

	public Location teleToLocation(Location loc, int randomOffset) {
		int x = loc.getX();
		int y = loc.getY();
		int z = loc.getZ();

		if (this instanceof Player && DimensionalRiftManager.getInstance().checkIfInRiftZone(getX(), getY(), getZ(), true)) // true -> ignore waiting room :)
		{
			Player player = (Player) this;
			player.sendMessage("You have been sent to the waiting room.");
			if (player.isInParty() && player.getParty().isInDimensionalRift()) {
				player.getParty().getDimensionalRift().usedTeleport(player);
			}
			int[] newCoords = DimensionalRiftManager.getInstance().getRoom((byte) 0, (byte) 0).getTeleportCoords();
			x = newCoords[0];
			y = newCoords[1];
			z = newCoords[2];
		}
		return teleToLocation(x, y, z, randomOffset);
	}

	public Location teleToLocation(TeleportType teleportWhere) {
		return teleToLocation(MapRegionTable.getInstance().getLocationToTeleport(this, teleportWhere), 20);
	}

	// =========================================================
	// Method - Private
	/**
	 * Launch a physical attack against a target (Simple, Bow, Pole or
	 * Dual).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Get the active weapon (always equipped in the right hand)</li>
	 * </ul>
	 * <ul>
	 * <li>If weapon is a bow, check for arrows, MP and bow re-use delay (if
	 * necessary, equip the Player with arrows in left hand)</li>
	 * <li>If weapon is a bow, consume MP and set the new period of bow non
	 * re-use</li>
	 * </ul>
	 * <ul>
	 * <li>Get the Attack Speed of the Creature (delay (in milliseconds) before
	 * next attack)</li>
	 * <li>Select the type of attack to start (Simple, Bow, Pole or Dual) and
	 * verify if SoulShot are charged then start calculation</li>
	 * <li>If the Server->Client packet Attack contains at least 1 hit, send the
	 * Server->Client packet Attack to the Creature AND to all Player in the
	 * _KnownPlayers of the Creature</li>
	 * <li>Notify AI with EVT_READY_TO_ACT</li>
	 * </ul>
	 *
	 * @param target The Creature targeted
	 */
	public void doAttack(Creature target) {
		if (target == null || isAttackingDisabled()) {
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (!isAlikeDead()) {
			if (isNpc() && target.isAlikeDead() || !getKnownType(Creature.class).contains(target)) {
				getAI().setIntention(CtrlIntention.ACTIVE);
				sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}

			if (isPlayer() && target.isDead()) {
				getAI().setIntention(CtrlIntention.ACTIVE);
				sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}

		final Player player = getPlayer();

		if (player != null && player.isInObserverMode()) {
			if(target.getPlayer().getAppearance().isInvisible()) {
				removeTarget();
				getAI().setIntention(CtrlIntention.IDLE);
			}
			sendPacket(SystemMessage.getSystemMessage(SystemMessageId.OBSERVERS_CANNOT_PARTICIPATE));
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		// Checking if target has moved to peace zone
		if (isInsidePeaceZone(this, target)) {
			getAI().setIntention(CtrlIntention.ACTIVE);
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		stopEffectsOnAction();

		// Get the active weapon item corresponding to the active weapon instance (always equipped in the right hand)
		if (getAttackType() == WeaponType.FISHINGROD) {
			// You can't make an attack with a fishing pole.
			getAI().setIntention(CtrlIntention.IDLE);
			sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANNOT_ATTACK_WITH_FISHING_POLE));
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		// GeoData Los Check here (or dz > 1000)
		if (!GeoEngine.getInstance().canSeeTarget(this, target)) {
			getAI().setIntention(CtrlIntention.ACTIVE);
			sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANT_SEE_TARGET));
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		final Weapon weapon = getActiveWeaponItem();
		// Check for a bow
		if (isPlayer()) {
			if (getAttackType() == WeaponType.BOW) {
				// Equip arrows needed in left hand and send ItemList to the L2PcINstance then return True
				if (!checkAndEquipArrows()) {
					// Cancel the action because the Player have no arrow
					getAI().setIntention(CtrlIntention.IDLE);
					sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ARROWS));
					sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}

				// Verify if the bow can be use
				final long timeToNextBowAttack = disableBowAttackEndTime - System.currentTimeMillis();
				if (timeToNextBowAttack > 0) {
					// Cancel the action because the bow can't be re-use at this moment
					ThreadPool.schedule(new NotifyAITask(this, CtrlEvent.EVT_READY_TO_ACT), timeToNextBowAttack);
					sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}

				/* MP CONSUME ATTACK ***********************************/
				final int mpConsume = weapon.getMpConsume(this);
				if (mpConsume > 0) {
					if (getCurrentMp() < mpConsume) {
						// If Player doesn't have enough MP, stop the attack
						ThreadPool.schedule(new NotifyAITask(this, CtrlEvent.EVT_READY_TO_ACT), 100);
						sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_MP));
						sendPacket(ActionFailed.STATIC_PACKET);
						return;
					} else {
						getStatus().reduceMp(mpConsume);
					}
				}
			}

			/* HP CONSUME ATTACK ***********************************/
			if (weapon != null) {
				final int hpConsume = getActiveWeaponItem().getHpConsume(this);
				if (hpConsume > 0) {
					// we not stop attack if HP not enough, we just kill the attacker
					reduceCurrentHp(hpConsume, this, null);
					if (isDead()) {
						return;
					}
				}
			}
		} else if (isNpc()) {
			if (disableBowAttackEndTime > System.currentTimeMillis()) {
				return;
			}
		}

		_move = null;

		final AbstractHit attackHit = getAttackType().createHit(this, target);

		// flagging
		if (player != null) {
			AttackStanceTaskManager.getInstance().add(player);
			if (player.getActiveSummon() != target) {
				player.updatePvPStatus(target);
			}
		}

		if (!attackHit.start()) {
			abortAttack();
		} else {
			// IA implementation for ON_ATTACK_ACT (mob which attacks a player).
			if (isAttackableInstance()) {
				try {
					// Bypass behavior if the victim isn't a player
					Player victim = target.getPlayer();
					if (victim != null) {
						Npc mob = ((Npc) this);
						List<Quest> quests = mob.getTemplate().getEventQuests(EventType.ON_ATTACK_ACT);
						if (quests != null) {
							for (Quest quest : quests) {
								quest.notifyAttackAct(mob, victim);
							}
						}
					}
				} catch (Exception e) {
					_log.error("", e);
				}
			}

			// If we didn't miss the hit, discharge the shoulshots, if any
			setChargedShot(ShotType.SOULSHOT, false);

			if (player != null) {
				if (player.isCursedWeaponEquipped()) {
					// If hitted by a cursed weapon, Cp is reduced to 0
					if (!target.isInvul()) {
						target.setCurrentCp(0);
					}
				} else if (player.isHero()) {
					if (target.isPlayer() && target.getPlayer().isCursedWeaponEquipped()) // If a cursed weapon is hitted by a Hero, Cp is reduced to 0
					{
						target.setCurrentCp(0);
					}
				}
			}
		}

		// If the Server->Client packet Attack contains at least 1 hit, send the Server->Client packet Attack
		// to the Creature AND to all Player in the _KnownPlayers of the Creature
		if (attackHit.getAttack().hasHits()) {
			broadcastPacket(attackHit.getAttack());
		}

		// Notify AI with EVT_READY_TO_ACT
		ThreadPool.schedule(new NotifyAITask(this, CtrlEvent.EVT_READY_TO_ACT), attackHit.getHitTime());
	}

	/**
	 * Manage the casting task (casting and interrupt time, re-use delay...) and
	 * display the casting bar and animation on client.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Verify the possibilty of the the cast : skill is a spell, caster
	 * isn't muted...</li>
	 * <li>Get the list of all targets (ex : area effects) and define the
	 * L2Charcater targeted (its stats will be used in calculation)</li>
	 * <li>Calculate the casting time (base + modifier of MAtkSpd), interrupt
	 * time and re-use delay</li>
	 * <li>Send MagicSkillUse (to diplay casting animation), a packet SetupGauge
	 * (to display casting bar) and a system message</li>
	 * <li>Disable all skills during the casting time (create a task
	 * EnableAllSkills)</li>
	 * <li>Disable the skill during the re-use delay (create a task
	 * EnableSkill)</li>
	 * <li>Create a task MagicUseTask (that will call method onMagicUseTimer) to
	 * launch the Magic Skill at the end of the casting time</li>
	 * </ul>
	 *
	 * @param skill The L2Skill to use
	 * @param simulate is simulate cast or not
	 */
	public void doCast(L2Skill skill, boolean simulate) {
		cast = new Cast(this, skill, simulate);
		cast.start();
	}

	public void doCast(L2Skill skill) {
		doCast(skill, false);
	}

	/**
	 * Check if casting of skill is possible
	 *
	 * @param skill
	 * @return True if casting is possible
	 */
	public boolean checkDoCastConditions(L2Skill skill) {
		if (skill == null || isSkillDisabled(skill)) {
			// Send ActionFailed to the Player
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}

		// Check if the caster has enough MP
		if (getCurrentMp() < getStat().getMpConsume(skill) + getStat().getMpInitialConsume(skill)) {
			// Send a System Message to the caster
			sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_MP));

			// Send ActionFailed to the Player
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}

		// Check if the caster has enough HP
		if (getCurrentHp() <= skill.getHpConsume()) {
			// Send a System Message to the caster
			sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_HP));

			// Send ActionFailed to the Player
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}

		// Verify the different types of silence (magic and physic)
		if (isMuted(skill.getAlignment())) {
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}

		// Check if the caster owns the weapon needed
		if (!skill.getWeaponDependancy(this)) {
			// Send ActionFailed to the Player
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}

		// Check if the spell consumes an Item
		if (skill.getItemConsumeId() > 0 && getInventory() != null) {
			// Get the ItemInstance consumed by the spell
			ItemInstance requiredItems = getInventory().getItemByItemId(skill.getItemConsumeId());

			// Check if the caster owns enough consumed Item to cast
			if (requiredItems == null || requiredItems.getCount() < skill.getItemConsume()) {
				// Checked: when a summon skill failed, server show required consume item count
				if (skill.getSkillType() == ESkillType.SUMMON) {
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.SUMMONING_SERVITOR_COSTS_S2_S1);
					sm.addItemName(skill.getItemConsumeId());
					sm.addNumber(skill.getItemConsume());
					sendPacket(sm);
					return false;
				}

				sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NUMBER_INCORRECT));
				return false;
			}
		}

		return true;
	}

	/**
	 * Index according to skill id the current timestamp of use, overridden in
	 * Player.
	 *
	 * @param skill id
	 * @param reuse delay
	 */
	public void addTimeStamp(L2Skill skill, long reuse) {
	}

	public void startFusionSkill(Creature target, L2Skill skill) {
		if (skill.getSkillType() != ESkillType.FUSION) {
			return;
		}

		if (_fusionSkill == null) {
			_fusionSkill = new FusionSkill(this, target, skill);
		}
	}

	/**
	 * Kill the Creature.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Set target to null and cancel Attack or Cast</li>
	 * <li>Stop movement</li>
	 * <li>Stop HP/MP/CP Regeneration task</li>
	 * <li>Stop all active skills effects in progress on the Creature</li>
	 * <li>Send the Server->Client packet StatusUpdate with current HP and MP to
	 * all other Player to inform</li>
	 * <li>Notify Creature AI</li>
	 * </ul>
	 * <B><U> Overridden in </U> :</B>
	 * <ul>
	 * <li>L2Npc : Create a DecayTask to remove the corpse of the L2Npc after 7
	 * seconds</li>
	 * <li>L2Attackable : Distribute rewards (EXP, SP, Drops...) and notify
	 * Quest Engine</li>
	 * <li>Player : Apply Death Penalty, Manage gain/loss Karma and Item
	 * Drop</li>
	 * </ul>
	 *
	 * @param killer The Creature who killed it
	 * @return true if successful.
	 */
	public boolean doDie(Creature killer) {
		// killing is only possible one time
		synchronized (this) {
			if (isDead()) {
				return false;
			}

			// now reset currentHp to zero
			setCurrentHp(0);

			setIsDead(true);
		}

		// Set target to null and cancel Attack or Cast
		setTarget(null);

		// Stop movement
		stopMove(null);

		// Stop Regeneration task, and removes all current effects
		getStatus().stopRegen(ERegenType.VALUES);

		stopAllEffectsExceptThoseThatLastThroughDeath();

		calculateRewards(killer);

		// Send the Server->Client packet StatusUpdate with current HP and MP to all other Player to inform
		broadcastStatusUpdate();

		// Notify Creature AI
		if (hasAI()) {
			getAI().notifyEvent(CtrlEvent.EVT_DEAD, null);
		}

		final WorldRegion region = getRegion();
		if (region != null) {
			region.onDeath(this);
		}

		eventBus.notify(new OnKill(killer, this));
		
		if (killer.isPlayer()) {
			final RandomQuestComponent component = killer.getComponent(RandomQuestComponent.class);
			if (component.hasQuest()) {
				component.getQuest().getType().getHandler().getEventBus().notify(new OnKill(killer, this));
			}
		}
		return true;
	}

	public void deleteMe() {
		if (hasAI()) {
			getAI().stopAITask();
		}
	}

	public void detachAI() {
		_ai = null;
	}

	protected void calculateRewards(Creature killer) {
	}

	/**
	 * Sets HP, MP and CP and revives the Creature.
	 */
	public void doRevive() {
		if (!isDead() || isTeleporting()) {
			return;
		}

		setIsDead(false);

		_status.setCurrentHp(getMaxHp() * Config.RESPAWN_RESTORE_HP);

		// Start broadcast status
		broadcastPacket(new Revive(this));

		final WorldRegion region = getRegion();
		if (region != null) {
			region.onRevive(this);
		}
	}

	/**
	 * Revives the Creature using skill.
	 *
	 * @param revivePower
	 */
	public void doRevive(double revivePower) {
		doRevive();
	}

	/**
	 * @return the CreatureAI of the Creature and if its null create a new one.
	 */
	public CreatureAI getAI() {
		CreatureAI ai = _ai;
		if (ai == null) {
			synchronized (this) {
				if (_ai == null) {
					_ai = new CreatureAI(this);
				}

				return _ai;
			}
		}
		return ai;
	}

	public void setAI(CreatureAI newAI) {
		CreatureAI oldAI = getAI();
		if (oldAI != null && oldAI != newAI && oldAI instanceof AttackableAI) {
			((AttackableAI) oldAI).stopAITask();
		}

		_ai = newAI;
	}

	/**
	 * @return True if the Creature has a CreatureAI.
	 */
	public boolean hasAI() {
		return _ai != null;
	}

	/**
	 * @return True if the Creature is RaidBoss or his minion.
	 */
	public boolean isRaid() {
		return _isRaid;
	}

	/**
	 * Set this Npc as a Raid instance.
	 *
	 * @param isRaid
	 */
	public void setIsRaid(boolean isRaid) {
		_isRaid = isRaid;
	}

	/**
	 * @return True if the Creature is minion.
	 */
	public boolean isMinion() {
		return false;
	}

	/**
	 * @return True if the Creature is Raid minion.
	 */
	public boolean isRaidMinion() {
		return false;
	}

	public final L2Skill getLastSimultaneousSkillCast() {
		return _lastSimultaneousSkillCast;
	}

	public void setLastSimultaneousSkillCast(L2Skill skill) {
		_lastSimultaneousSkillCast = skill;
	}

	public final L2Skill getLastSkillCast() {
		return _lastSkillCast;
	}

	public void setLastSkillCast(L2Skill skill) {
		_lastSkillCast = skill;
	}

	public final boolean isNoRndWalk() {
		return _isNoRndWalk;
	}

	public final void setIsNoRndWalk(boolean value) {
		_isNoRndWalk = value;
	}

	public final boolean isAfraid() {
		return isAffected(EEffectFlag.FEAR);
	}

	public final boolean isConfused() {
		return isAffected(EEffectFlag.CONFUSED);
	}

	public final boolean isMuted(ESkillAlignmentType alignment) {
		return isAffected(alignment.getEffectFlag());
	}

	public final boolean isPhysicalMuted() {
		return isAffected(EEffectFlag.PHYSICAL_MUTED);
	}

	public final boolean isRooted() {
		return isAffected(EEffectFlag.ROOTED);
	}

	public final boolean isSleeping() {
		return isAffected(EEffectFlag.SLEEP);
	}

	public final boolean isStunned() {
		return isAffected(EEffectFlag.STUNNED);
	}

	public final boolean isBetrayed() {
		return isAffected(EEffectFlag.BETRAYED);
	}

	public final boolean isImmobileUntilAttacked() {
		return isAffected(EEffectFlag.MEDITATING);
	}

	/**
	 * @return True if the Creature can't use its skills (ex : stun, sleep...).
	 */
	public final boolean isAllSkillsDisabled() {
		return _allSkillsDisabled || isStunned() || isImmobileUntilAttacked() || isSleeping() || isParalyzed();
	}

	/**
	 * BEWARE : don't use isAttackingNow() instead of _attackEndTime >
	 * System.currentTimeMillis(), as it's overidden on L2Summon.
	 *
	 * @return True if the Creature can't attack (stun, sleep, attackEndTime,
	 * fakeDeath, paralyse).
	 */
	public boolean isAttackingDisabled() {
		return isAttackingDisabled || isFlying() || isStunned() || isImmobileUntilAttacked() || isSleeping() || attackEndTime > System.currentTimeMillis() || isParalyzed() || isAlikeDead() || isCoreAIDisabled();
	}

	public final Calculator[] getCalculators() {
		return _calculators;
	}

	public boolean isImmobilized() {
		return _isImmobilized;
	}

	public void setIsImmobilized(boolean value) {
		_isImmobilized = value;
	}

	/**
	 * @return True if the Creature is dead or use fake death.
	 */
	public boolean isAlikeDead() {
		return _isDead;
	}

	/**
	 * @return True if the Creature is dead.
	 */
	public final boolean isDead() {
		return _isDead;
	}

	public final void setIsDead(boolean value) {
		_isDead = value;
	}

	/**
	 * @return True if the Creature is in a state where he can't move.
	 */
	public boolean isMovementDisabled() {
		return isStunned() || isImmobileUntilAttacked() || isRooted() || isSleeping() || isOverloaded() || isParalyzed() || isImmobilized() || isAlikeDead() || isTeleporting();
	}

	/**
	 * @return True if the Creature is in a state where he can't be controlled.
	 */
	public boolean isOutOfControl() {
		return isOutOfControl || isConfused() || isAfraid() || isParalyzed() || isStunned() || isSleeping() || isOnMovie;
	}

	public final boolean isOverloaded() {
		return _isOverloaded;
	}

	public final void setIsOverloaded(boolean value) {
		_isOverloaded = value;
	}

	public final boolean isParalyzed() {
		return _isParalyzed || isAffected(EEffectFlag.PARALYZED);
	}

	public final void setIsParalyzed(boolean value) {
		_isParalyzed = value;
	}

	public boolean isSeated() {
		return false;
	}

	public boolean isRiding() {
		return false;
	}

	public boolean isFlying() {
		return false;
	}

	public final boolean isRunning() {
		return _isRunning;
	}

	public final void setIsRunning(boolean value) {
		_isRunning = value;
		if (getMoveSpeed() != 0) {
			broadcastPacket(new ChangeMoveType(this));
		}

		if (this instanceof Player) {
			((Player) this).broadcastUserInfo();
		} else if (this instanceof Summon) {
			((Summon) this).broadcastStatusUpdate();
		} else if (this instanceof Npc) {
			for (Player player : getKnownType(Player.class)) {
				if (getMoveSpeed() == 0) {
					player.sendPacket(new ServerObjectInfo((Npc) this, player));
				} else {
					player.sendPacket(new NpcInfo((Npc) this, player));
				}
			}
		}
	}

	/**
	 * Set the Creature movement type to run and send Server->Client packet
	 * ChangeMoveType to all others Player.
	 */
	public final void setRunning() {
		if (!isRunning()) {
			setIsRunning(true);
		}
	}

	public final boolean isTeleporting() {
		return _isTeleporting;
	}

	public final void setIsTeleporting(boolean value) {
		_isTeleporting = value;
	}

	public void setIsInvul(boolean b) {
		_isInvul = b;
	}

	public boolean isInvul() {
		return _isInvul || _isTeleporting || isOnMovie;
	}

	public void setIsMortal(boolean b) {
		_isMortal = b;
	}

	public boolean isMortal() {
		return _isMortal;
	}

	public boolean isUndead() {
		return false;
	}

	public void initCharStat() {
		_stat = new CreatureStat(this);
	}

	public CreatureStat getStat() {
		return _stat;
	}

	public final void setStat(CreatureStat value) {
		_stat = value;
	}

	public void initCharStatus() {
		_status = new CreatureStatus(this);
	}

	public CreatureStatus getStatus() {
		return _status;
	}

	public final void setStatus(CreatureStatus value) {
		_status = value;
	}

	public CreatureTemplate getTemplate() {
		return _template;
	}

	/**
	 * Set the template of the Creature.<BR>
	 * <BR>
	 * Each Creature owns generic and static properties (ex : all Keltir have
	 * the same number of HP...). All of those properties are stored in a
	 * different template for each type of Creature. Each template is loaded
	 * once in the server cache memory (reduce memory use). When a new instance
	 * of Creature is spawned, server just create a link between the instance
	 * and the template This link is stored in <B>_template</B>
	 *
	 * @param template The template to set up.
	 */
	protected final void setTemplate(CreatureTemplate template) {
		_template = template;
	}

	/**
	 * @return the Title of the Creature.
	 */
	public final String getTitle() {
		return _title;
	}

	/**
	 * Set the Title of the Creature. Concatens it if length > 16.
	 *
	 * @param value The String to test.
	 */
	public void setTitle(String value) {
		if (value == null) {
			_title = "";
		} else if (value.length() > 16) {
			_title = value.substring(0, 15);
		} else {
			_title = value;
		}
	}

	/**
	 * Set the Creature movement type to walk and send Server->Client packet
	 * ChangeMoveType to all others Player.
	 */
	public final void setWalking() {
		if (isRunning()) {
			setIsRunning(false);
		}
	}

	// =========================================================
	/**
	 * Map 32 bits (0x0000) containing all abnormal effect in progress
	 */
	private int _AbnormalEffects;

	protected CharEffectList _effects = new CharEffectList(this);

	// Method - Public
	/**
	 * Launch and add L2Effect (including Stack Group management) to Creature
	 * and update client magic icone.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress on the Creature are identified in
	 * ConcurrentHashMap(Integer,L2Effect) <B>_effects</B>. The Integer key of
	 * _effects is the L2Skill Identifier that has created the L2Effect.<BR>
	 * <BR>
	 * Several same effect can't be used on a Creature at the same time. Indeed,
	 * effects are not stackable and the last cast will replace the previous in
	 * progress. More, some effects belong to the same Stack Group (ex WindWald
	 * and Haste Potion). If 2 effects of a same group are used at the same time
	 * on a Creature, only the more efficient (identified by its priority order)
	 * will be preserve.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Add the L2Effect to the Creature _effects</li>
	 * <li>If this effect doesn't belong to a Stack Group, add its Funcs to the
	 * Calculator set of the Creature (remove the old one if necessary)</li>
	 * <li>If this effect has higher priority in its Stack Group, add its Funcs
	 * to the Calculator set of the Creature (remove previous stacked effect
	 * Funcs if necessary)</li>
	 * <li>If this effect has NOT higher priority in its Stack Group, set the
	 * effect to Not In Use</li>
	 * <li>Update active skills in progress icones on player client</li>
	 * </ul>
	 *
	 * @param newEffect
	 */
	public void addEffect(L2Effect newEffect) {
		_effects.queueEffect(newEffect, false);
	}

	public void addEffects(L2Skill skill, Creature effector, Creature effected) {
		final List<L2Effect> effects = skill.getEffects(effector, effected);
		for (int i = 0; i < effects.size(); i++) {
			final L2Effect effect = effects.get(i);
			_effects.queueEffect(effect, false);
		}
	}

	/**
	 * Stop and remove L2Effect (including Stack Group management) from Creature
	 * and update client magic icone.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress on the Creature are identified in
	 * ConcurrentHashMap(Integer,L2Effect) <B>_effects</B>. The Integer key of
	 * _effects is the L2Skill Identifier that has created the L2Effect.<BR>
	 * <BR>
	 * Several same effect can't be used on a Creature at the same time. Indeed,
	 * effects are not stackable and the last cast will replace the previous in
	 * progress. More, some effects belong to the same Stack Group (ex WindWald
	 * and Haste Potion). If 2 effects of a same group are used at the same time
	 * on a Creature, only the more efficient (identified by its priority order)
	 * will be preserve.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Remove Func added by this effect from the Creature Calculator (Stop
	 * L2Effect)</li>
	 * <li>If the L2Effect belongs to a not empty Stack Group, replace theses
	 * Funcs by next stacked effect Funcs</li>
	 * <li>Remove the L2Effect from _effects of the Creature</li>
	 * <li>Update active skills in progress icones on player client</li>
	 * </ul>
	 *
	 * @param effect
	 */
	public final void removeEffect(L2Effect effect) {
		_effects.queueEffect(effect, true);
	}

	public final void startAbnormalEffect(AbnormalEffect mask) {
		_AbnormalEffects |= mask.getMask();
		updateAbnormalEffect();
	}

	public final void startAbnormalEffect(int mask) {
		_AbnormalEffects |= mask;
		updateAbnormalEffect();
	}

	public final void stopAbnormalEffect(AbnormalEffect mask) {
		_AbnormalEffects &= ~mask.getMask();
		updateAbnormalEffect();
	}

	public final void stopAbnormalEffect(int mask) {
		_AbnormalEffects &= ~mask;
		updateAbnormalEffect();
	}

	/**
	 * Stop all active skills effects in progress on the Creature.<BR>
	 * <BR>
	 */
	public void stopAllEffects() {
		_effects.stopAllEffects();
	}

	public void stopAllEffectsExceptThoseThatLastThroughDeath() {
		_effects.stopAllEffectsExceptThoseThatLastThroughDeath();
	}

	/**
	 * Confused
	 */
	public final void startConfused() {
		getAI().notifyEvent(CtrlEvent.EVT_CONFUSED);
		updateAbnormalEffect();
	}

	public final void stopConfused(L2Effect effect) {
		if (effect == null) {
			stopEffects(L2EffectType.CONFUSION);
		} else {
			removeEffect(effect);
		}

		if (!(this instanceof Player)) {
			getAI().notifyEvent(CtrlEvent.EVT_THINK);
		}
		updateAbnormalEffect();
	}

	/**
	 * Fake Death
	 */
	public final void startFakeDeath() {
		if (!(this instanceof Player)) {
			return;
		}

		((Player) this).setIsFakeDeath(true);
		abortAttack();
		abortCast();
		stopMove(null);
		getAI().notifyEvent(CtrlEvent.EVT_FAKE_DEATH);
		broadcastPacket(new ChangeWaitType(this, ChangeWaitType.WT_START_FAKEDEATH));
	}

	public final void stopFakeDeath(boolean removeEffects) {
		if (!(this instanceof Player)) {
			return;
		}

		final Player player = ((Player) this);

		if (removeEffects) {
			stopEffects(L2EffectType.FAKE_DEATH);
		}

		// if this is a player instance, start the grace period for this character (grace from mobs only)!
		player.setIsFakeDeath(false);
		player.setRecentFakeDeath();

		broadcastPacket(new ChangeWaitType(this, ChangeWaitType.WT_STOP_FAKEDEATH));
		broadcastPacket(new Revive(this));

		// Schedule a paralyzed task to wait for the animation to finish
		ThreadPool.schedule(new Runnable() {
			@Override
			public void run() {
				setIsParalyzed(false);
			}
		}, (int) (2000 / getStat().getMovementSpeedMultiplier()));
		setIsParalyzed(true);
	}

	/**
	 * Fear
	 */
	public final void startFear() {
		abortAttack();
		abortCast();
		stopMove(null);
		getAI().notifyEvent(CtrlEvent.EVT_AFRAID);
		updateAbnormalEffect();
	}

	public final void stopFear(boolean removeEffects) {
		if (removeEffects) {
			stopEffects(L2EffectType.FEAR);
		}
		updateAbnormalEffect();
	}

	/**
	 * ImmobileUntilAttacked
	 */
	public final void startImmobileUntilAttacked() {
		abortAttack();
		abortCast();
		stopMove(null);
		getAI().notifyEvent(CtrlEvent.EVT_SLEEPING);
		updateAbnormalEffect();
	}

	public final void stopImmobileUntilAttacked(L2Effect effect) {
		if (effect == null) {
			stopEffects(L2EffectType.IMMOBILEUNTILATTACKED);
		} else {
			removeEffect(effect);
			stopSkillEffects(effect.getSkill().getId());
		}

		getAI().notifyEvent(CtrlEvent.EVT_THINK, null);
		updateAbnormalEffect();
	}

	/**
	 * Muted
	 */
	public final void startMuted() {
		abortCast();
		getAI().notifyEvent(CtrlEvent.EVT_MUTED);
		updateAbnormalEffect();
	}

	public final void stopMuted(boolean removeEffects) {
		if (removeEffects) {
			stopEffects(L2EffectType.SILENCE);
		}

		updateAbnormalEffect();
	}

	/**
	 * Paralize
	 */
	public final void startParalyze() {
		abortAttack();
		abortCast();
		stopMove(null);
		getAI().notifyEvent(CtrlEvent.EVT_PARALYZED);
	}

	public final void stopParalyze(boolean removeEffects) {
		if (removeEffects) {
			stopEffects(L2EffectType.PARALYZE);
		}

		if (!(this instanceof Player)) {
			getAI().notifyEvent(CtrlEvent.EVT_THINK);
		}
	}

	/**
	 * Root
	 */
	public final void startRooted() {
		stopMove(null);
		getAI().notifyEvent(CtrlEvent.EVT_ROOTED);
		updateAbnormalEffect();
	}

	public final void stopRooting(boolean removeEffects) {
		if (removeEffects) {
			stopEffects(L2EffectType.ROOT);
		}

		if (!(this instanceof Player)) {
			getAI().notifyEvent(CtrlEvent.EVT_THINK);
		}
		updateAbnormalEffect();
	}

	/**
	 * Sleep
	 */
	public final void startSleeping() {
		/* Aborts any attacks/casts if slept */
		abortAttack();
		abortCast();
		stopMove(null);
		getAI().notifyEvent(CtrlEvent.EVT_SLEEPING);
		updateAbnormalEffect();
	}

	public final void stopSleeping(boolean removeEffects) {
		if (removeEffects) {
			stopEffects(L2EffectType.SLEEP);
		}

		if (!(this instanceof Player)) {
			getAI().notifyEvent(CtrlEvent.EVT_THINK);
		}
		updateAbnormalEffect();
	}

	/**
	 * Stun
	 */
	public final void startStunning() {
		/* Aborts any attacks/casts if stunned */
		abortAttack();
		abortCast();
		stopMove(null);
		getAI().notifyEvent(CtrlEvent.EVT_STUNNED);

		if (!(this instanceof Summon)) {
			getAI().setIntention(CtrlIntention.IDLE);
		}

		updateAbnormalEffect();
	}

	public final void stopStunning(boolean removeEffects) {
		if (removeEffects) {
			stopEffects(L2EffectType.STUN);
		}

		if (!(this instanceof Player)) {
			getAI().notifyEvent(CtrlEvent.EVT_THINK);
		}
		updateAbnormalEffect();
	}

	/**
	 * Stop and remove the L2Effects corresponding to the L2Skill Identifier and
	 * update client magic icon.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress on the Creature are identified in
	 * ConcurrentHashMap(Integer,L2Effect) <B>_effects</B>. The Integer key of
	 * _effects is the L2Skill Identifier that has created the L2Effect.<BR>
	 * <BR>
	 *
	 * @param skillId The L2Skill Identifier of the L2Effect to remove from
	 * _effects
	 */
	public final void stopSkillEffects(int skillId) {
		_effects.stopSkillEffects(skillId);
	}

	/**
	 * Stop and remove the L2Effects corresponding to the L2SkillType and update
	 * client magic icon.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress on the Creature are identified in
	 * ConcurrentHashMap(Integer,L2Effect) <B>_effects</B>. The Integer key of
	 * _effects is the L2Skill Identifier that has created the L2Effect.<BR>
	 * <BR>
	 *
	 * @param skillType The L2SkillType of the L2Effect to remove from _effects
	 * @param negateLvl
	 */
	public final void stopSkillEffects(ESkillType skillType, int negateLvl) {
		_effects.stopSkillEffects(skillType, negateLvl);
	}

	public final void stopSkillEffects(ESkillType skillType) {
		_effects.stopSkillEffects(skillType, -1);
	}

	/**
	 * Stop and remove all L2Effect of the selected type (ex : BUFF,
	 * DMG_OVER_TIME...) from the Creature and update client magic icone.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress on the Creature are identified in
	 * ConcurrentHashMap(Integer,L2Effect) <B>_effects</B>. The Integer key of
	 * _effects is the L2Skill Identifier that has created the L2Effect.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Remove Func added by this effect from the Creature Calculator (Stop
	 * L2Effect)</li>
	 * <li>Remove the L2Effect from _effects of the Creature</li>
	 * <li>Update active skills in progress icones on player client</li>
	 * </ul>
	 *
	 * @param type The type of effect to stop ((ex : BUFF, DMG_OVER_TIME...)
	 */
	public final void stopEffects(L2EffectType type) {
		_effects.stopEffects(type);
	}

	/**
	 * Exits all buffs effects of the skills with "removedOnAnyAction" set.
	 * Called on any action except movement (attack, cast).
	 */
	public final void stopEffectsOnAction() {
		_effects.stopEffectsOnAction();
	}

	/**
	 * Exits all buffs effects of the skills with "removedOnDamage" set. Called
	 * on decreasing HP and mana burn.
	 *
	 * @param awake
	 */
	public final void stopEffectsOnDamage(boolean awake) {
		_effects.stopEffectsOnDamage(awake);
	}

	/**
	 * <B><U> Overridden in</U> :</B>
	 * <ul>
	 * <li>L2Npc</li>
	 * <li>Player</li>
	 * <li>L2Summon</li>
	 * <li>L2DoorInstance</li>
	 * </ul>
	 * <BR>
	 */
	public abstract void updateAbnormalEffect();

	/**
	 * Update active skills in progress (In Use and Not In Use because stacked)
	 * icones on client.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress (In Use and Not In Use because
	 * stacked) are represented by an icone on the client.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method ONLY UPDATE the
	 * client of the player and not clients of all players in the
	 * party.</B></FONT><BR>
	 * <BR>
	 */
	public final void updateEffectIcons() {
		updateEffectIcons(false);
	}

	/**
	 * Updates Effect Icons for this character(palyer/summon) and his party if
	 * any<BR>
	 * Overridden in:
	 * <ul>
	 * <li>Player</li>
	 * <li>L2Summon</li>
	 * </ul>
	 *
	 * @param partyOnly
	 */
	public void updateEffectIcons(boolean partyOnly) {
		// overridden
	}

	/**
	 * In Server->Client packet, each effect is represented by 1 bit of the map
	 * (ex : BLEEDING = 0x0001 (bit 1), SLEEP = 0x0080 (bit 8)...). The map is
	 * calculated by applying a BINARY OR operation on each effect.
	 *
	 * @return a map of 16 bits (0x0000) containing all abnormal effect in
	 * progress for this Creature.
	 */
	public int getAbnormalEffect() {
		int ae = _AbnormalEffects;
		if (isStunned()) {
			ae |= AbnormalEffect.STUN.getMask();
		}
		if (isRooted()) {
			ae |= AbnormalEffect.ROOT.getMask();
		}
		if (isSleeping()) {
			ae |= AbnormalEffect.SLEEP.getMask();
		}
		if (isConfused()) {
			ae |= AbnormalEffect.FEAR.getMask();
		}
		if (isAfraid()) {
			ae |= AbnormalEffect.FEAR.getMask();
		}
		if (isImmobileUntilAttacked()) {
			ae |= AbnormalEffect.FLOATING_ROOT.getMask();
		}
		for (ESkillAlignmentType alignment : ESkillAlignmentType.values()) {
			if (isMuted(alignment)) {
				ae |= AbnormalEffect.MUTED.getMask();
			}
		}
		return ae;
	}

	/**
	 * Return all active skills effects in progress on the Creature.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress on the Creature are identified in
	 * <B>_effects</B>. The Integer key of _effects is the L2Skill Identifier
	 * that has created the effect.<BR>
	 * <BR>
	 *
	 * @return A table containing all active skills effect in progress on the
	 * Creature
	 */
	public final L2Effect[] getAllEffects() {
		return _effects.getAllEffects();
	}

	/**
	 * Return L2Effect in progress on the Creature corresponding to the L2Skill
	 * Identifier.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress on the Creature are identified in
	 * <B>_effects</B>.
	 *
	 * @param skillId The L2Skill Identifier of the L2Effect to return from the
	 * _effects
	 * @return The L2Effect corresponding to the L2Skill Identifier
	 */
	public final L2Effect getFirstEffect(int skillId) {
		return _effects.getFirstEffect(skillId);
	}

	/**
	 * Return the first L2Effect in progress on the Creature created by the
	 * L2Skill.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress on the Creature are identified in
	 * <B>_effects</B>.
	 *
	 * @param skill The L2Skill whose effect must be returned
	 * @return The first L2Effect created by the L2Skill
	 */
	public final L2Effect getFirstEffect(L2Skill skill) {
		return _effects.getFirstEffect(skill);
	}

	/**
	 * Return the first L2Effect in progress on the Creature corresponding to
	 * the Effect Type (ex : BUFF, STUN, ROOT...).<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All active skills effects in progress on the Creature are identified in
	 * ConcurrentHashMap(Integer,L2Effect) <B>_effects</B>. The Integer key of
	 * _effects is the L2Skill Identifier that has created the L2Effect.<BR>
	 * <BR>
	 *
	 * @param tp The Effect Type of skills whose effect must be returned
	 * @return The first L2Effect corresponding to the Effect Type
	 */
	public final L2Effect getFirstEffect(L2EffectType tp) {
		return _effects.getFirstEffect(tp);
	}

	/**
	 * This class group all mouvement data.<BR>
	 * <BR>
	 * <B><U> Data</U> :</B>
	 * <ul>
	 * <li>_moveTimestamp : Last time position update</li>
	 * <li>_xDestination, _yDestination, _zDestination : Position of the
	 * destination</li>
	 * <li>_xMoveFrom, _yMoveFrom, _zMoveFrom : Position of the origin</li>
	 * <li>_moveStartTime : Start time of the movement</li>
	 * <li>_ticksToMove : Nb of ticks between the start and the destination</li>
	 * <li>_xSpeedTicks, _ySpeedTicks : Speed in unit/ticks</li>
	 * </ul>
	 */
	public static class MoveData {

		// when we retrieve x/y/z we use GameTimeControl.getGameTicks()
		// if we are moving, but move timestamp==gameticks, we don't need
		// to recalculate position
		public long _moveStartTime;
		public long _moveTimestamp; // last update
		public int _xDestination;
		public int _yDestination;
		public int _zDestination;
		public double _xAccurate; // otherwise there would be rounding errors
		public double _yAccurate;
		public double _zAccurate;
		public int _heading;

		public boolean disregardingGeodata;
		public int onGeodataPathIndex;
		public List<Location> geoPath;
		public int geoPathAccurateTx;
		public int geoPathAccurateTy;
		public int geoPathGtx;
		public int geoPathGty;
	}

	/**
	 * Table containing all skillId that are disabled
	 */
	private final Map<Integer, Long> _disabledSkills = new ConcurrentHashMap<>();
	private boolean _allSkillsDisabled;

	/**
	 * Movement data of this Creature
	 */
	protected MoveData _move;

	/**
	 * Orientation of the Creature
	 */
	private int _heading;

	/**
	 * WorldObject targeted by the Creature
	 */
	private WorldObject _target;

	// set by the start of attack, in game ticks
	@Getter
	@Setter
	private long attackEndTime;
	@Getter
	@Setter
	private long disableBowAttackEndTime;

	protected CreatureAI _ai;

	/**
	 * Future Skill Cast
	 */
	@Getter
	@Setter
	protected Future<?> skillCast;
	@Getter
	@Setter
	protected Future<?> skillCast2;
	@Getter
	@Setter
	protected Cast cast;

	/**
	 * Add a Func to the Calculator set of the Creature.
	 *
	 * @param f The Func object to add to the Calculator corresponding to the
	 * state affected
	 */
	public final void addStatFunc(Func f) {
		if (f == null) {
			return;
		}

		// Select the Calculator of the affected state in the Calculator set
		int stat = f.stat.ordinal();

		synchronized (_calculators) {
			if (_calculators[stat] == null) {
				_calculators[stat] = new Calculator();
			}

			// Add the Func to the calculator corresponding to the state
			_calculators[stat].addFunc(f);
		}
	}

	/**
	 * Add a list of Funcs to the Calculator set of the Creature.
	 *
	 * @param funcs The list of Func objects to add to the Calculator
	 * corresponding to the state affected
	 */
	public final void addStatFuncs(List<Func> funcs) {
		List<Stats> modifiedStats = new ArrayList<>();
		for (Func f : funcs) {
			modifiedStats.add(f.stat);
			addStatFunc(f);
		}
		broadcastModifiedStats(modifiedStats);
	}

	/**
	 * Remove all Func objects with the selected owner from the Calculator set
	 * of the Creature.
	 *
	 * @param owner The Object(Skill, Item...) that has created the effect
	 */
	public final void removeStatsByOwner(Object owner) {
		List<Stats> modifiedStats = null;

		int i = 0;
		// Go through the Calculator set
		synchronized (_calculators) {
			for (Calculator calc : _calculators) {
				if (calc != null) {
					// Delete all Func objects of the selected owner
					if (modifiedStats != null) {
						modifiedStats.addAll(calc.removeOwner(owner));
					} else {
						modifiedStats = calc.removeOwner(owner);
					}

					if (calc.size() == 0) {
						_calculators[i] = null;
					}
				}
				i++;
			}

			if (owner instanceof L2Effect) {
				if (!((L2Effect) owner).preventExitUpdate) {
					broadcastModifiedStats(modifiedStats);
				}
			} else {
				broadcastModifiedStats(modifiedStats);
			}
		}
	}

	private void broadcastModifiedStats(List<Stats> stats) {
		if (stats == null || stats.isEmpty()) {
			return;
		}

		boolean broadcastFull = false;
		StatusUpdate su = null;

		if (this instanceof Summon && ((Summon) this).getPlayer() != null) {
			((Summon) this).updateAndBroadcastStatusAndInfos(1);
		} else {
			for (Stats stat : stats) {
				if (stat == Stats.PAtkSpd) {
					if (su == null) {
						su = new StatusUpdate(this);
					}

					su.addAttribute(StatusUpdate.ATK_SPD, getPAtkSpd());
				} else if (stat == Stats.MAtkSpd) {
					if (su == null) {
						su = new StatusUpdate(this);
					}

					su.addAttribute(StatusUpdate.CAST_SPD, getMAtkSpd());
				} else if (stat == Stats.MaxHP && this instanceof Attackable) {
					if (su == null) {
						su = new StatusUpdate(this);
					}

					su.addAttribute(StatusUpdate.MAX_HP, getMaxHp());
				} else if (stat == Stats.Speed) {
					broadcastFull = true;
				}
			}
		}

		if (this instanceof Player) {
			if (broadcastFull) {
				((Player) this).updateAndBroadcastStatus(2);
			} else {
				((Player) this).updateAndBroadcastStatus(1);
				if (su != null) {
					broadcastPacket(su);
				}
			}
		} else if (this instanceof Npc) {
			if (broadcastFull) {
				for (Player player : getKnownType(Player.class)) {
					if (getMoveSpeed() == 0) {
						player.sendPacket(new ServerObjectInfo((Npc) this, player));
					} else {
						player.sendPacket(new NpcInfo((Npc) this, player));
					}
				}
			} else if (su != null) {
				broadcastPacket(su);
			}
		} else if (su != null) {
			broadcastPacket(su);
		}
	}

	/**
	 * @return the orientation of the Creature.
	 */
	public final int getHeading() {
		return _heading;
	}

	/**
	 * Set the orientation of the Creature.
	 *
	 * @param heading
	 */
	public final void setHeading(int heading) {
		_heading = heading;
	}

	public final int getXdestination() {
		MoveData m = _move;
		if (m != null) {
			return m._xDestination;
		}

		return getX();
	}

	public final int getYdestination() {
		MoveData m = _move;
		if (m != null) {
			return m._yDestination;
		}

		return getY();
	}

	public final int getZdestination() {
		MoveData m = _move;
		if (m != null) {
			return m._zDestination;
		}

		return getZ();
	}

	/**
	 * @return True if the Creature is in combat.
	 */
	public boolean isInCombat() {
		return hasAI() && getAI().isAutoAttacking();
	}

	/**
	 * @return True if the Creature is moving.
	 */
	public final boolean isMoving() {
		return _move != null;
	}

	/**
	 * @return True if the Creature is travelling a calculated path.
	 */
	public final boolean isOnGeodataPath() {
		MoveData m = _move;
		if (m == null) {
			return false;
		}

		if (m.onGeodataPathIndex == -1) {
			return false;
		}

		if (m.onGeodataPathIndex == m.geoPath.size() - 1) {
			return false;
		}

		return true;
	}

	/**
	 * @return True if the Creature is casting.
	 */
	public final boolean isCastingNow() {
		return _isCastingNow;
	}

	public void setIsCastingNow(boolean value) {
		_isCastingNow = value;
	}

	public final boolean isCastingSimultaneouslyNow() {
		return _isCastingSimultaneouslyNow;
	}

	public void setIsCastingSimultaneouslyNow(boolean value) {
		_isCastingSimultaneouslyNow = value;
	}

	/**
	 * @return True if the cast of the Creature can be aborted.
	 */
	public final boolean canAbortCast() {
		return cast != null && cast.getInterruptTime() > System.currentTimeMillis();
	}

	/**
	 * @return True if the Creature is attacking.
	 */
	public boolean isAttackingNow() {
		return attackEndTime > System.currentTimeMillis();
	}

	/**
	 * Abort the attack of the Creature and send Server->Client ActionFailed
	 * packet.
	 */
	public final void abortAttack() {
		if (isAttackingNow()) {
			sendPacket(ActionFailed.STATIC_PACKET);
		}
	}

	/**
	 * Abort the cast of the Creature and send Server->Client
	 * MagicSkillCanceld/ActionFailed packet.<BR>
	 * <BR>
	 */
	public final void abortCast() {
		if (isCastingNow() || isCastingSimultaneouslyNow()) {
			Future<?> future = skillCast;
			// cancels the skill hit scheduled task
			if (future != null) {
				future.cancel(true);
				skillCast = null;
			}
			future = skillCast2;
			if (future != null) {
				future.cancel(true);
				skillCast2 = null;
			}

			if (getFusionSkill() != null) {
				getFusionSkill().onCastAbort();
			}

			L2Effect mog = getFirstEffect(L2EffectType.SIGNET_GROUND);
			if (mog != null) {
				mog.exit();
			}

			if (_allSkillsDisabled) {
				enableAllSkills(); // this remains for forced skill use, e.g. scroll of escape
			}
			setIsCastingNow(false);
			setIsCastingSimultaneouslyNow(false);

			// safeguard for cannot be interrupt any more
			if (cast != null) {
				cast.setInterruptTime(0);
			}

			if (this instanceof Playable) {
				getAI().notifyEvent(CtrlEvent.EVT_FINISH_CASTING); // setting back previous intention
			}
			broadcastPacket(new MagicSkillCanceld(getObjectId())); // broadcast packet to stop animations client-side
			sendPacket(ActionFailed.STATIC_PACKET); // send an "action failed" packet to the caster
		}
	}

	/**
	 * Update the position of the Creature during a movement and return True if
	 * the movement is finished.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * At the beginning of the move action, all properties of the movement are
	 * stored in the MoveData object called <B>_move</B> of the Creature. The
	 * position of the start point and of the destination permit to estimated in
	 * function of the movement speed the time to achieve the destination.<BR>
	 * <BR>
	 * When the movement is started (ex : by MovetoLocation), this method will
	 * be called each 0.1 sec to estimate and update the Creature position on
	 * the server. Note, that the current server position can differe from the
	 * current client position even if each movement is straight foward. That's
	 * why, client send regularly a Client->Server ValidatePosition packet to
	 * eventually correct the gap on the server. But, it's always the server
	 * position that is used in range calculation.<BR>
	 * <BR>
	 * At the end of the estimated movement time, the Creature position is
	 * automatically set to the destination position even if the movement is not
	 * finished.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : The current Z position is
	 * obtained FROM THE CLIENT by the Client->Server ValidatePosition Packet.
	 * But x and y positions must be calculated to avoid that players try to
	 * modify their movement speed.</B></FONT><BR>
	 * <BR>
	 *
	 * @return True if the movement is finished
	 */
	public boolean updatePosition() {
		// Get movement data
		MoveData m = _move;

		if (m == null) {
			return true;
		}

		if (!isVisible()) {
			_move = null;
			return true;
		}

		// Check if this is the first update
		if (m._moveTimestamp == 0) {
			m._moveTimestamp = m._moveStartTime;
			m._xAccurate = getX();
			m._yAccurate = getY();
		}

		// get current time
		final long time = System.currentTimeMillis();

		// Check if the position has already been calculated
		if (m._moveTimestamp > time) {
			return false;
		}

		int xPrev = getX();
		int yPrev = getY();
		int zPrev = getZ(); // the z coordinate may be modified by coordinate synchronizations

		double dx, dy, dz;
		if (Config.COORD_SYNCHRONIZE == 1) {
			// the only method that can modify x,y while moving (otherwise _move would/should be set null)
			dx = m._xDestination - xPrev;
			dy = m._yDestination - yPrev;
		} else {
			// otherwise we need saved temporary values to avoid rounding errors
			dx = m._xDestination - m._xAccurate;
			dy = m._yDestination - m._yAccurate;
		}

		final boolean isFloating = isFlying() || isInsideZone(ZoneId.WATER);

		// Z coordinate will follow geodata or client values once a second to reduce possible cpu load
		if (Config.COORD_SYNCHRONIZE == 2 && !isFloating && !m.disregardingGeodata && Rnd.get(10) == 0 && GeoEngine.getInstance().hasGeo(xPrev, yPrev)) {
			short geoHeight = GeoEngine.getInstance().getHeight(xPrev, yPrev, zPrev);
			dz = m._zDestination - geoHeight;
			// quite a big difference, compare to validatePosition packet
			if (this instanceof Player && Math.abs(((Player) this).getClientZ() - geoHeight) > 200 && Math.abs(((Player) this).getClientZ() - geoHeight) < 1500) {
				// allow diff
				dz = m._zDestination - zPrev;
			} // allow mob to climb up to pcinstance
			else if (isInCombat() && Math.abs(dz) > 200 && (dx * dx + dy * dy) < 40000) {
				// climbing
				dz = m._zDestination - zPrev;
			} else {
				zPrev = geoHeight;
			}
		} else {
			dz = m._zDestination - zPrev;
		}

		double delta = dx * dx + dy * dy;
		// close enough, allows error between client and server geodata if it cannot be avoided
		// should not be applied on vertical movements in water or during flight
		if (delta < 10000 && (dz * dz > 2500) && !isFloating) {
			delta = Math.sqrt(delta);
		} else {
			delta = Math.sqrt(delta + dz * dz);
		}

		double distFraction = Double.MAX_VALUE;
		if (delta > 1) {
			final double distPassed = (getStat().getMoveSpeed() * (time - m._moveTimestamp)) / 1000;
			distFraction = distPassed / delta;
		}

		// already there, Set the position of the Creature to the destination
		if (distFraction > 1) {
			setXYZ(m._xDestination, m._yDestination, m._zDestination);
		} else {
			m._xAccurate += dx * distFraction;
			m._yAccurate += dy * distFraction;

			// Set the position of the Creature to estimated after parcial move
			setXYZ((int) (m._xAccurate), (int) (m._yAccurate), zPrev + (int) (dz * distFraction + 0.5));
		}
		revalidateZone(false);

		// Set the timer of last position update to now
		m._moveTimestamp = time;

		return (distFraction > 1);
	}

	public void revalidateZone(boolean force) {
		if (getRegion() == null) {
			return;
		}

		// This function is called too often from movement code
		if (force) {
			_zoneValidateCounter = 4;
		} else {
			_zoneValidateCounter--;
			if (_zoneValidateCounter < 0) {
				_zoneValidateCounter = 4;
			} else {
				return;
			}
		}
		getRegion().revalidateZones(this);
	}

	/**
	 * Stop movement of the Creature (called by AI Accessor only).
	 * <ul>
	 * <li>Delete movement data of the Creature</li>
	 * <li>Set the current position and refresh the region if necessary</li>
	 * </ul>
	 *
	 * @param loc : The SpawnLocation where the character must stop.
	 */
	public void stopMove(SpawnLocation loc) {
		// Delete movement data of the Creature
		_move = null;

		// Set the current position and refresh the region if necessary.
		if (loc != null) {
			setXYZ(loc.getX(), loc.getY(), loc.getZ());
			setHeading(loc.getHeading());
			revalidateZone(true);
		}
		broadcastPacket(new StopMove(this));
	}

	/**
	 * @return Returns the showSummonAnimation.
	 */
	public boolean isShowSummonAnimation() {
		return _showSummonAnimation;
	}

	/**
	 * @param showSummonAnimation The showSummonAnimation to set.
	 */
	public void setShowSummonAnimation(boolean showSummonAnimation) {
		_showSummonAnimation = showSummonAnimation;
	}

	/**
	 * Target an object. If the object is invisible, we set it to null.<br>
	 * <B><U>Overridden in Player</U></B> : Remove the Player from the old
	 * target _statusListener and add it to the new target if it was a Creature
	 *
	 * @param object WorldObject to target
	 */
	public void setTarget(WorldObject object) {
		if (object != null && !object.isVisible()) {
			object = null;
		}

		_target = object;
	}

	/**
	 * @return the identifier of the WorldObject targeted or -1.
	 */
	public final int getTargetId() {
		return (_target != null) ? _target.getObjectId() : -1;
	}

	/**
	 * @return the WorldObject targeted or null.
	 */
	public final WorldObject getTarget() {
		return _target;
	}

	/**
	 * Calculate movement data for a move to location action and add the
	 * Creature to movingObjects of GameTimeController (only called by AI
	 * Accessor).<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * At the beginning of the move action, all properties of the movement are
	 * stored in the MoveData object called <B>_move</B> of the Creature. The
	 * position of the start point and of the destination permit to estimated in
	 * function of the movement speed the time to achieve the destination.<BR>
	 * <BR>
	 * All Creature in movement are identified in <B>movingObjects</B> of
	 * GameTimeController that will call the updatePosition method of those
	 * Creature each 0.1s.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Get current position of the Creature</li>
	 * <li>Calculate distance (dx,dy) between current position and destination
	 * including offset</li>
	 * <li>Create and Init a MoveData object</li>
	 * <li>Set the Creature _move object to MoveData object</li>
	 * <li>Add the Creature to movingObjects of the GameTimeController</li>
	 * <li>Create a task to notify the AI that Creature arrives at a check point
	 * of the movement</li>
	 * </ul>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T send
	 * Server->Client packet MoveToPawn/MoveToLocation </B></FONT><BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B>
	 * <ul>
	 * <li>AI : onIntentionMoveTo(L2CharPosition),
	 * onIntentionPickUp(WorldObject), onIntentionInteract(WorldObject)</li>
	 * <li>FollowTask</li>
	 * </ul>
	 *
	 * @param x The X position of the destination
	 * @param y The Y position of the destination
	 * @param z The Y position of the destination
	 * @param offset The size of the interaction area of the Creature targeted
	 */
	public void moveToLocation(int x, int y, int z, int offset) {
		// get movement speed of character
		double speed = getStat().getMoveSpeed();
		if (speed <= 0 || isMovementDisabled()) {
			return;
		}

		// get current position of character
		final int curX = getX();
		final int curY = getY();
		final int curZ = getZ();

		// calculate distance (dx, dy, dz) between current position and new destination
		// TODO: improve Z axis move/follow support when dx,dy are small compared to dz
		double dx = (x - curX);
		double dy = (y - curY);
		double dz = (z - curZ);
		double distance = Math.sqrt(dx * dx + dy * dy);

		// check vertical movement
		final boolean verticalMovementOnly = isFlying() && distance == 0 && dz != 0;
		if (verticalMovementOnly) {
			distance = Math.abs(dz);
		}

		// TODO: really necessary?
		// adjust target XYZ when swiming in water (can be easily over 3000)
		if (isInsideZone(ZoneId.WATER) && distance > 700) {
			double divider = 700 / distance;
			x = curX + (int) (divider * dx);
			y = curY + (int) (divider * dy);
			z = curZ + (int) (divider * dz);
			dx = (x - curX);
			dy = (y - curY);
			dz = (z - curZ);
			distance = Math.sqrt(dx * dx + dy * dy);
		}

		// debug distance
		if (Config.DEBUG) {
			_log.info("distance to target:" + distance);
		}

		double cos;
		double sin;

		// Check if a movement offset is defined or no distance to go through
		if (offset > 0 || distance < 1) {
			// approximation for moving closer when z coordinates are different
			// TODO: handle Z axis movement better
			offset -= Math.abs(dz);
			if (offset < 5) {
				offset = 5;
			}

			// If no distance to go through, the movement is canceled
			if (distance < 1 || distance - offset <= 0) {
				// Notify the AI that the Creature is arrived at destination
				getAI().notifyEvent(CtrlEvent.EVT_ARRIVED);
				return;
			}

			// Calculate movement angles needed
			sin = dy / distance;
			cos = dx / distance;

			distance -= (offset - 5); // due to rounding error, we have to move a bit closer to be in range

			// Calculate the new destination with offset included
			x = curX + (int) (distance * cos);
			y = curY + (int) (distance * sin);
		} else {
			// Calculate movement angles needed
			sin = dy / distance;
			cos = dx / distance;
		}

		// get new MoveData
		MoveData newMd = new MoveData();

		// initialize new MoveData
		newMd.onGeodataPathIndex = -1;
		newMd.disregardingGeodata = false;

		// flying chars not checked - even canSeeTarget doesn't work yet
		// swimming also not checked unless in siege zone - but distance is limited
		// npc walkers not checked
		if (!isFlying() && (!isInsideZone(ZoneId.WATER) || isInsideZone(ZoneId.SIEGE)) && !(this instanceof Walker)) {
			final boolean isInVehicle = this instanceof Player && ((Player) this).getVehicle() != null;
			if (isInVehicle) {
				newMd.disregardingGeodata = true;
			}

			double originalDistance = distance;
			int originalX = x;
			int originalY = y;
			int originalZ = z;
			int gtx = (originalX - World.WORLD_X_MIN) >> 4;
			int gty = (originalY - World.WORLD_Y_MIN) >> 4;

			// Movement checks:
			// when geodata == 2, for all characters except mobs returning home (could be changed later to teleport if pathfinding fails)
			// when geodata == 1, for l2playableinstance and l2riftinstance only
			// assuming intention_follow only when following owner
			if ((Config.PATHFINDING && !(this instanceof Attackable && ((Attackable) this).isReturningToSpawnPoint())) || (this instanceof Player && !(isInVehicle && distance > 1500)) || (this instanceof Summon && !(getAI().getIntention() == CtrlIntention.FOLLOW)) || isAfraid() || this instanceof RiftInvader) {
				if (isOnGeodataPath()) {
					try {
						if (gtx == _move.geoPathGtx && gty == _move.geoPathGty) {
							return;
						}

						_move.onGeodataPathIndex = -1; // Set not on geodata path
					} catch (NullPointerException e) {
						// nothing
					}
				}

				if (curX < World.WORLD_X_MIN || curX > World.WORLD_X_MAX || curY < World.WORLD_Y_MIN || curY > World.WORLD_Y_MAX) {
					// Temporary fix for character outside world region errors
					_log.warn("Character " + getName() + " outside world area, in coordinates x:" + curX + " y:" + curY);
					getAI().setIntention(CtrlIntention.IDLE);

					if (this instanceof Player) {
						((Player) this).logout();
					} else if (this instanceof Summon) {
						return; // prevention when summon get out of world coords, player will not loose him, unsummon handled from pcinstance
					} else {
						onDecay();
					}

					return;
				}

				// location different if destination wasn't reached (or just z coord is different)
				Location destiny = GeoEngine.getInstance().canMoveToTargetLoc(curX, curY, curZ, x, y, z);
				x = destiny.getX();
				y = destiny.getY();
				z = destiny.getZ();
				dx = x - curX;
				dy = y - curY;
				dz = z - curZ;
				distance = verticalMovementOnly ? Math.abs(dz * dz) : Math.sqrt(dx * dx + dy * dy);
			}

			// Pathfinding checks. Only when geodata setting is 2, the LoS check gives shorter result than the original movement was and the LoS gives a shorter distance than 2000
			// This way of detecting need for pathfinding could be changed.
			if (Config.PATHFINDING && originalDistance - distance > 30 && distance < 2000 && !isAfraid()) {
				// Path calculation -- overrides previous movement check
				if ((this instanceof Playable && !isInVehicle) || isMinion() || isInCombat()) {
					newMd.geoPath = GeoEngine.getInstance().findPath(curX, curY, curZ, originalX, originalY, originalZ, this instanceof Playable);
					if (newMd.geoPath == null || newMd.geoPath.size() < 2) {
						// No path found
						// Even though there's no path found (remember geonodes aren't perfect), the mob is attacking and right now we set it so that the mob will go after target anyway, is dz is small enough.
						// With cellpathfinding this approach could be changed but would require taking off the geonodes and some more checks.
						// Summons will follow their masters no matter what.
						// Currently minions also must move freely since L2AttackableAI commands them to move along with their leader
						if (this instanceof Player || (!(this instanceof Playable) && !isMinion() && Math.abs(z - curZ) > 140) || (this instanceof Summon && !((Summon) this).getFollowStatus())) {
							return;
						}

						newMd.disregardingGeodata = true;
						x = originalX;
						y = originalY;
						z = originalZ;
						distance = originalDistance;
					} else {
						newMd.onGeodataPathIndex = 0; // on first segment
						newMd.geoPathGtx = gtx;
						newMd.geoPathGty = gty;
						newMd.geoPathAccurateTx = originalX;
						newMd.geoPathAccurateTy = originalY;

						x = newMd.geoPath.get(newMd.onGeodataPathIndex).getX();
						y = newMd.geoPath.get(newMd.onGeodataPathIndex).getY();
						z = newMd.geoPath.get(newMd.onGeodataPathIndex).getZ();

						dx = x - curX;
						dy = y - curY;
						dz = z - curZ;
						distance = verticalMovementOnly ? Math.abs(dz * dz) : Math.sqrt(dx * dx + dy * dy);
						sin = dy / distance;
						cos = dx / distance;
					}
				}
			}

			// If no distance to go through, the movement is canceled
			if (distance < 1 && (Config.PATHFINDING || this instanceof Playable || this instanceof RiftInvader || isAfraid())) {
				if (this instanceof Summon) {
					((Summon) this).setFollowStatus(false);
				}

				getAI().setIntention(CtrlIntention.IDLE);
				return;
			}
		}

		// Apply Z distance for flying or swimming for correct timing calculations
		if ((isFlying() || isInsideZone(ZoneId.WATER)) && !verticalMovementOnly) {
			distance = Math.sqrt(distance * distance + dz * dz);
		}

		// Caclulate the Nb of ticks between the current position and the destination
		newMd._xDestination = x;
		newMd._yDestination = y;
		newMd._zDestination = z;

		// Calculate and set the heading of the Creature
		newMd._heading = 0;

		newMd._moveStartTime = System.currentTimeMillis();

		// set new MoveData as character MoveData
		_move = newMd;

		// Does not broke heading on vertical movements
		if (!verticalMovementOnly) {
			setHeading(MathUtil.calculateHeadingFrom(cos, sin));
		}

		// add the character to moving objects of the GameTimeController
		MovementTaskManager.getInstance().add(this);
	}

	public boolean moveToNextRoutePoint() {
		// character is not on geodata path, return
		if (!isOnGeodataPath()) {
			_move = null;
			return false;
		}

		// character movement is not allowed, return
		if (getStat().getMoveSpeed() <= 0 || isMovementDisabled()) {
			_move = null;
			return false;
		}

		// get current MoveData
		MoveData oldMd = _move;

		// get new MoveData
		MoveData newMd = new MoveData();

		// initialize new MoveData
		newMd.onGeodataPathIndex = oldMd.onGeodataPathIndex + 1;
		newMd.geoPath = oldMd.geoPath;
		newMd.geoPathGtx = oldMd.geoPathGtx;
		newMd.geoPathGty = oldMd.geoPathGty;
		newMd.geoPathAccurateTx = oldMd.geoPathAccurateTx;
		newMd.geoPathAccurateTy = oldMd.geoPathAccurateTy;

		if (oldMd.onGeodataPathIndex == oldMd.geoPath.size() - 2) {
			newMd._xDestination = oldMd.geoPathAccurateTx;
			newMd._yDestination = oldMd.geoPathAccurateTy;
			newMd._zDestination = oldMd.geoPath.get(newMd.onGeodataPathIndex).getZ();
		} else {
			newMd._xDestination = oldMd.geoPath.get(newMd.onGeodataPathIndex).getX();
			newMd._yDestination = oldMd.geoPath.get(newMd.onGeodataPathIndex).getY();
			newMd._zDestination = oldMd.geoPath.get(newMd.onGeodataPathIndex).getZ();
		}

		newMd._heading = 0;
		newMd._moveStartTime = System.currentTimeMillis();

		// set new MoveData as character MoveData
		_move = newMd;

		// get travel distance
		double dx = (_move._xDestination - super.getX());
		double dy = (_move._yDestination - super.getY());
		double distance = Math.sqrt(dx * dx + dy * dy);

		// set character heading
		if (distance != 0) {
			setHeading(MathUtil.calculateHeadingFrom(dx, dy));
		}

		// add the character to moving objects of the GameTimeController
		MovementTaskManager.getInstance().add(this);

		// send MoveToLocation packet to known objects
		broadcastPacket(new MoveToLocation(this));
		return true;
	}

	public boolean validateMovementHeading(int heading) {
		MoveData m = _move;

		if (m == null) {
			return true;
		}

		boolean result = true;
		if (m._heading != heading) {
			result = (m._heading == 0); // initial value or false
			m._heading = heading;
		}

		return result;
	}

	/**
	 * Return the squared distance between the current position of the Creature
	 * and the given object.
	 *
	 * @param object WorldObject
	 * @return the squared distance
	 */
	public final double getDistanceSq(WorldObject object) {
		return getDistanceSq(object.getX(), object.getY(), object.getZ());
	}

	/**
	 * Return the squared distance between the current position of the Creature
	 * and the given x, y, z.
	 *
	 * @param x X position of the target
	 * @param y Y position of the target
	 * @param z Z position of the target
	 * @return the squared distance
	 */
	public final double getDistanceSq(int x, int y, int z) {
		double dx = x - getX();
		double dy = y - getY();
		double dz = z - getZ();

		return (dx * dx + dy * dy + dz * dz);
	}

	/**
	 * Return the squared plan distance between the current position of the
	 * Creature and the given x, y, z.<BR>
	 * (check only x and y, not z)
	 *
	 * @param x X position of the target
	 * @param y Y position of the target
	 * @return the squared plan distance
	 */
	public final double getPlanDistanceSq(int x, int y) {
		double dx = x - getX();
		double dy = y - getY();

		return (dx * dx + dy * dy);
	}

	/**
	 * Check if this object is inside the given radius around the given object.
	 * Warning: doesn't cover collision radius!
	 *
	 * @param object the target
	 * @param radius the radius around the target
	 * @param checkZ should we check Z axis also
	 * @param strictCheck true if (distance < radius), false if (distance <=
	 * radius)
	 * @retu
	 * rn true is the Creature is inside the radius.
	 */
	public final boolean isInsideRadius(WorldObject object, int radius, boolean checkZ, boolean strictCheck) {
		return isInsideRadius(object.getX(), object.getY(), object.getZ(), radius, checkZ, strictCheck);
	}

	/**
	 * Check if this object is inside the given plan radius around the given
	 * point. Warning: doesn't cover collision radius!
	 *
	 * @param x X position of the target
	 * @param y Y position of the target
	 * @param radius the radius around the target
	 * @param strictCheck true if (distance < radius), false if (distance <=
	 * radius)
	 * @retu
	 * rn true is the Creature is inside the radius.
	 */
	public final boolean isInsideRadius(int x, int y, int radius, boolean strictCheck) {
		return isInsideRadius(x, y, 0, radius, false, strictCheck);
	}

	/**
	 * Check if this object is inside the given radius around the given point.
	 *
	 * @param x X position of the target
	 * @param y Y position of the target
	 * @param z Z position of the target
	 * @param radius the radius around the target
	 * @param checkZ should we check Z axis also
	 * @param strictCheck true if (distance < radius), false if (distance <=
	 * radius)
	 * @retu
	 * rn true is the Creature is inside the radius.
	 */
	public final boolean isInsideRadius(int x, int y, int z, int radius, boolean checkZ, boolean strictCheck) {
		double dx = x - getX();
		double dy = y - getY();
		double dz = z - getZ();

		if (strictCheck) {
			if (checkZ) {
				return (dx * dx + dy * dy + dz * dz) < radius * radius;
			}

			return (dx * dx + dy * dy) < radius * radius;
		}

		if (checkZ) {
			return (dx * dx + dy * dy + dz * dz) <= radius * radius;
		}

		return (dx * dx + dy * dy) <= radius * radius;
	}

	/**
	 * @return True if arrows are available.
	 */
	public boolean checkAndEquipArrows() {
		return true;
	}

	/**
	 * Add Exp and Sp to the Creature.
	 *
	 * @param addToExp An int value.
	 * @param addToSp An int value.
	 */
	public void addExpAndSp(long addToExp, int addToSp) {
		// Dummy method (overridden by players and pets)
	}

	/**
	 * @return the active weapon instance (always equipped in the right hand).
	 */
	public abstract ItemInstance getActiveWeaponInstance();

	/**
	 * @return the active weapon item (always equipped in the right hand).
	 */
	public abstract Weapon getActiveWeaponItem();

	/**
	 * @return the secondary weapon instance (always equipped in the left hand).
	 */
	public abstract ItemInstance getSecondaryWeaponInstance();

	/**
	 * @return the secondary {@link Item} item (always equiped in the left
	 * hand).
	 */
	public abstract Item getSecondaryWeaponItem();

	/**
	 * Break an attack and send Server->Client ActionFailed packet and a System
	 * Message to the Creature.
	 */
	public void breakAttack() {
		if (isAttackingNow()) {
			// Abort the attack of the Creature and send Server->Client ActionFailed packet
			abortAttack();

			if (this instanceof Player) {
				sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ATTACK_FAILED));
			}
		}
	}

	/**
	 * Break a cast and send Server->Client ActionFailed packet and a System
	 * Message to the Creature.
	 */
	public void breakCast() {
		// damage can only cancel magical skills
		if (isCastingNow() && canAbortCast() && getLastSkillCast() != null && getLastSkillCast().isMagic()) {
			// Abort the cast of the Creature and send Server->Client MagicSkillCanceld/ActionFailed packet.
			abortCast();

			if (this instanceof Player) {
				sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CASTING_INTERRUPTED));
			}
		}
	}

	/**
	 * Reduce the arrow number of the Creature.<BR>
	 * <BR>
	 * <B><U> Overriden in </U> :</B><BR>
	 * <BR>
	 * <li>Player</li><BR>
	 * <BR>
	 */
	public void reduceArrowCount() {
		// default is to do nothing
	}

	@Override
	public void onForcedAttack(Player player) {
		if (isInsidePeaceZone(player, this)) {
			// If Creature or target is in a peace zone, send a system message TARGET_IN_PEACEZONE ActionFailed
			player.sendPacket(SystemMessageId.TARGET_IN_PEACEZONE);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (player.isInOlympiadMode() && player.getTarget() != null && player.getTarget() instanceof Playable) {
			Player target = player.getTarget().getPlayer();
			if (target == null || (target.isInOlympiadMode() && (!player.isOlympiadStart() || player.getOlympiadGameId() != target.getOlympiadGameId()))) {
				// if Player is in Olympia and the match isn't already start, send ActionFailed
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}

		if (player.getTarget() != null && !player.getTarget().isAttackable() && !player.getAccessLevel().allowPeaceAttack()) {
			// If target is not attackable, send ActionFailed
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (player.isConfused()) {
			// If target is confused, send ActionFailed
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		// GeoData Los Check or dz > 1000
		if (!GeoEngine.getInstance().canSeeTarget(player, this)) {
			player.sendPacket(SystemMessageId.CANT_SEE_TARGET);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		// Notify AI with ATTACK
		player.getAI().setIntention(CtrlIntention.ATTACK, this);
	}

	/**
	 * This method checks if the player given as argument can interact with the
	 * L2Npc.
	 *
	 * @param player The player to test
	 * @return true if the player can interact with the L2Npc
	 */
	public boolean canInteract(Player player) {
		// Can't interact while casting a spell.
		if (player.isCastingNow() || player.isCastingSimultaneouslyNow()) {
			return false;
		}

		// Can't interact while died.
		if (player.isDead() || player.isFakeDeath()) {
			return false;
		}

		// Can't interact sitted.
		if (player.isSitting()) {
			return false;
		}

		// Can't interact in shop mode, or during a transaction or a request.
		if (player.isInStoreMode() || player.isProcessingTransaction()) {
			return false;
		}

		// Can't interact if regular distance doesn't match.
		if (!isInsideRadius(player, Npc.INTERACTION_DISTANCE, true, false)) {
			return false;
		}

		return true;
	}

	public static boolean isInsidePeaceZone(Creature attacker, WorldObject target) {
		if (target == null) {
			return false;
		}

		if (target instanceof Npc || attacker instanceof Npc) {
			return false;
		}

		// Summon or player check.
		if (attacker.getPlayer() != null && attacker.getPlayer().getAccessLevel().allowPeaceAttack()) {
			return false;
		}

		if (Config.KARMA_PLAYER_CAN_BE_KILLED_IN_PZ && target.getPlayer() != null && target.getPlayer().getKarma() > 0) {
			return false;
		}

		if (target instanceof Creature) {
			return target.isInsideZone(ZoneId.PEACE) || attacker.isInsideZone(ZoneId.PEACE);
		}

		return (MapRegionTable.getTown(target.getX(), target.getY(), target.getZ()) != null || attacker.isInsideZone(ZoneId.PEACE));
	}

	/**
	 * @return true if this character is inside an active grid.
	 */
	public boolean isInActiveRegion() {
		try {
			WorldRegion region = World.getInstance().getRegion(getX(), getY());
			return ((region != null) && (region.isActive()));
		} catch (Exception e) {
			if (this instanceof Player) {
				_log.warn("Player " + getName() + " at bad coords: (x: " + getX() + ", y: " + getY() + ", z: " + getZ() + ").");
				((Player) this).sendMessage("Error with your coordinates! Please reboot your game fully!");
				((Player) this).teleToLocation(80753, 145481, -3532, 0); // Near Giran luxury shop
			} else {
				_log.warn("Object " + getName() + " at bad coords: (x: " + getX() + ", y: " + getY() + ", z: " + getZ() + ").");
				decayMe();
			}
			return false;
		}
	}

	/**
	 * @return True if the Creature has a Party in progress.
	 */
	public boolean isInParty() {
		return false;
	}

	/**
	 * @return the L2Party object of the Creature.
	 */
	public Party getParty() {
		return null;
	}

	/**
	 * @return the type of attack, depending of the worn weapon.
	 */
	public WeaponType getAttackType() {
		final Weapon weapon = getActiveWeaponItem();
		if (weapon != null) {
			return weapon.getItemType();
		}

		return WeaponType.NONE;
	}

	public ChanceSkillList getChanceSkills() {
		return _chanceSkills;
	}

	public void removeChanceSkill(int id) {
		if (_chanceSkills == null) {
			return;
		}

		for (IChanceSkillTrigger trigger : _chanceSkills.keySet()) {
			if (!(trigger instanceof L2Skill)) {
				continue;
			}

			if (((L2Skill) trigger).getId() == id) {
				_chanceSkills.remove(trigger);
			}
		}
	}

	public void addChanceTrigger(IChanceSkillTrigger trigger) {
		if (_chanceSkills == null) {
			_chanceSkills = new ChanceSkillList(this);
		}

		_chanceSkills.put(trigger, trigger.getTriggeredChanceCondition());
	}

	public void removeChanceEffect(EffectChanceSkillTrigger effect) {
		if (_chanceSkills == null) {
			return;
		}

		_chanceSkills.remove(effect);
	}

	public void onStartChanceEffect() {
		if (_chanceSkills == null) {
			return;
		}

		_chanceSkills.onStart();
	}

	public void onActionTimeChanceEffect() {
		if (_chanceSkills == null) {
			return;
		}

		_chanceSkills.onActionTime();
	}

	public void onExitChanceEffect() {
		if (_chanceSkills == null) {
			return;
		}

		_chanceSkills.onExit();
	}

	/**
	 * This method is overidden on Player, L2Summon and L2Npc.
	 *
	 * @return the skills list of this Creature.
	 */
	public Map<Integer, L2Skill> getSkills() {
		return Collections.emptyMap();
	}

	/**
	 * Return the level of a skill owned by the Creature.
	 *
	 * @param skillId The identifier of the L2Skill whose level must be returned
	 * @return The level of the L2Skill identified by skillId
	 */
	public int getSkillLevel(int skillId) {
		final L2Skill skill = getSkills().get(skillId);

		return (skill == null) ? -1 : skill.getLevel();
	}

	/**
	 * @param skillId The identifier of the L2Skill to check the knowledge
	 * @return True if the skill is known by the Creature.
	 */
	public L2Skill getSkill(int skillId) {
		return getSkills().get(skillId);
	}

	/**
	 * Add a skill to the Creature _skills and its Func objects to the
	 * calculator set of the Creature.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All skills own by a Creature are identified in <B>_skills</B><BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Replace oldSkill by newSkill or Add the newSkill</li>
	 * <li>If an old skill has been replaced, remove all its Func objects of
	 * Creature calculator set</li>
	 * <li>Add Func objects of newSkill to the calculator set of the
	 * Creature</li>
	 * </ul>
	 * <B><U>Overriden in:</U></B>
	 * <ul>
	 * <li>Player : Save update in the character_skills table of the
	 * database</li>
	 * </ul>
	 *
	 * @param newSkill The L2Skill to add to the Creature
	 * @return The L2Skill replaced or null if just added a new L2Skill
	 */
	public L2Skill addSkill(L2Skill newSkill) {
		L2Skill oldSkill = null;

		if (newSkill != null) {
			// talents restore
			if (isPlayer() && newSkill.isTalent()) {
				final TalentBranchData branch = TalentBranchTable.getInstance().getBranch(getPlayer().getClassId());
				if (branch != null) {
					final TalentData talent = newSkill.getTalent();
					if (!branch.contains(talent.getId())) {
						log.warn("Talent {} not added cause its not from {} branch.", talent.getId(), getPlayer().getClassId());
						return null;
					}

					getPlayer().getTalentList().add(TalentTable.getInstance().get(talent.getId()));
				}
			}

			// Replace oldSkill by newSkill or Add the newSkill
			oldSkill = getSkills().put(newSkill.getId(), newSkill);

			// If an old skill has been replaced, remove all its Func objects
			if (oldSkill != null) {
				// if skill came with another one, we should delete the other one too.
				if (oldSkill.triggerAnotherSkill()) {
					removeSkill(oldSkill.getTriggeredId(), true);
				}

				removeStatsByOwner(oldSkill);
			}
			// Add Func objects of newSkill to the calculator set of the Creature
			addStatFuncs(newSkill.getStatFuncs(this));

			if (oldSkill != null && _chanceSkills != null) {
				removeChanceSkill(oldSkill.getId());
			}

			if (newSkill.isChance()) {
				addChanceTrigger(newSkill);
			}

			if (newSkill.isPassive()) {
				final List<L2Effect> effects = newSkill.getEffectsSelf(this);
				for (int i = 0; i < effects.size(); i++) {
					addEffect(effects.get(i));
				}
			}
		}

		return oldSkill;
	}

	/**
	 * Remove a skill from the Creature and its Func objects from calculator set
	 * of the Creature.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All skills own by a Creature are identified in <B>_skills</B><BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Remove the skill from the Creature _skills</li>
	 * <li>Remove all its Func objects from the Creature calculator set</li>
	 * </ul>
	 * <B><U> Overriden in </U> :</B>
	 * <ul>
	 * <li>Player : Save update in the character_skills table of the
	 * database</li>
	 * </ul>
	 *
	 * @param skill The L2Skill to remove from the Creature
	 * @return The L2Skill removed
	 */
	public L2Skill removeSkill(L2Skill skill) {
		if (skill == null) {
			return null;
		}

		return removeSkill(skill.getId(), true);
	}

	public L2Skill removeSkill(L2Skill skill, boolean cancelEffect) {
		if (skill == null) {
			return null;
		}

		// Remove the skill from the Creature _skills
		return removeSkill(skill.getId(), cancelEffect);
	}

	public L2Skill removeSkill(int skillId) {
		return removeSkill(skillId, true);
	}

	public L2Skill removeSkill(int skillId, boolean cancelEffect) {
		// Remove the skill from the Creature _skills
		L2Skill oldSkill = getSkills().remove(skillId);

		// Remove all its Func objects from the Creature calculator set
		if (oldSkill != null) {
			// clear talent list if needed
			if (oldSkill.isTalent() && isPlayer()) {
				getPlayer().getTalentList().clear();
			}

			// this is just a fail-safe againts buggers and gm dummies...
			if ((oldSkill.triggerAnotherSkill()) && oldSkill.getTriggeredId() > 0) {
				removeSkill(oldSkill.getTriggeredId(), true);
			}

			// Stop casting if this skill is used right now
			if (getLastSkillCast() != null && isCastingNow()) {
				if (oldSkill.getId() == getLastSkillCast().getId()) {
					abortCast();
				}
			}
			if (getLastSimultaneousSkillCast() != null && isCastingSimultaneouslyNow()) {
				if (oldSkill.getId() == getLastSimultaneousSkillCast().getId()) {
					abortCast();
				}
			}

			if (cancelEffect || oldSkill.isToggle() || oldSkill.isPassive()) {
				removeStatsByOwner(oldSkill);
				stopSkillEffects(oldSkill.getId());
			}

			if (oldSkill.isChance() && _chanceSkills != null) {
				removeChanceSkill(oldSkill.getId());
			}
		}

		return oldSkill;
	}

	/**
	 * Return the number of skills of type(Buff, Debuff, HEAL_PERCENT,
	 * MANAHEAL_PERCENT) affecting this Creature.
	 *
	 * @return The number of Buffs affecting this Creature
	 */
	public int getBuffCount() {
		return _effects.getBuffCount();
	}

	public int getDanceCount() {
		return _effects.getDanceCount();
	}

	// Quest event ON_SPELL_FINISHED
	public void notifyQuestEventSkillFinished(L2Skill skill, WorldObject target) {
	}

	public Map<Integer, Long> getDisabledSkills() {
		return _disabledSkills;
	}

	/**
	 * Enable a skill (remove it from _disabledSkills of the Creature).<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * All skills disabled are identified by their skillId in
	 * <B>_disabledSkills</B> of the Creature
	 *
	 * @param skill The L2Skill to enable
	 */
	public void enableSkill(L2Skill skill) {
		if (skill == null) {
			return;
		}

		_disabledSkills.remove(skill.getReuseHashCode());
	}

	/**
	 * Disable this skill id for the duration of the delay in milliseconds.
	 *
	 * @param skill
	 * @param delay (seconds * 1000)
	 */
	public void disableSkill(L2Skill skill, long delay) {
		if (skill == null) {
			return;
		}

		_disabledSkills.put(skill.getReuseHashCode(), (delay > 10) ? System.currentTimeMillis() + delay : Long.MAX_VALUE);
	}

	/**
	 * Check if a skill is disabled. All skills disabled are identified by their
	 * reuse hashcodes in <B>_disabledSkills</B>.
	 *
	 * @param skill The L2Skill to check
	 * @return true if the skill is currently disabled.
	 */
	public boolean isSkillDisabled(L2Skill skill) {
		if (_disabledSkills.isEmpty()) {
			return false;
		}

		if (skill == null || isAllSkillsDisabled()) {
			return true;
		}

		final int hashCode = skill.getReuseHashCode();

		final Long timeStamp = _disabledSkills.get(hashCode);
		if (timeStamp == null) {
			return false;
		}

		if (timeStamp < System.currentTimeMillis()) {
			_disabledSkills.remove(hashCode);
			return false;
		}

		return true;
	}

	/**
	 * Disable all skills (set _allSkillsDisabled to True).
	 */
	public void disableAllSkills() {
		_allSkillsDisabled = true;
	}

	/**
	 * Enable all skills (set _allSkillsDisabled to False).
	 */
	public void enableAllSkills() {
		_allSkillsDisabled = false;
	}

	/**
	 * Launch the magic skill and calculate its effects on each target contained
	 * in the targets table.
	 *
	 * @param skill The L2Skill to use
	 * @param targets The table of WorldObject targets
	 */
	public void callSkill(L2Skill skill, WorldObject[] targets) {
		try {
			// Check if the toggle skill effects are already in progress on the Creature
			if (skill.isToggle() && getFirstEffect(skill.getId()) != null) {
				return;
			}

			// Initial checks
			for (WorldObject trg : targets) {
				if (!(trg instanceof Creature)) {
					continue;
				}

				// Set some values inside target's instance for later use
				final Creature target = (Creature) trg;

				if (this instanceof Playable) {
					// Raidboss curse.
					if (!Config.RAID_DISABLE_CURSE) {
						boolean isVictimTargetBoss = false;

						// If the skill isn't offensive, we check extra things such as target's target.
						if (!skill.isOffensive()) {
							final WorldObject victimTarget = (target.hasAI()) ? target.getAI().getTarget() : null;
							if (victimTarget != null) {
								isVictimTargetBoss = victimTarget instanceof Creature && ((Creature) victimTarget).isRaid() && getLevel() > ((Creature) victimTarget).getLevel() + 8;
							}
						}

						// Target must be either a raid type, or if the skill is beneficial it checks the target's target.
						if ((target.isRaid() && getLevel() > target.getLevel() + 8) || isVictimTargetBoss) {
							final L2Skill curse = FrequentSkill.RAID_CURSE.getSkill();
							if (curse != null) {
								// Send visual and skill effects. Caster is the victim.
								broadcastPacket(new MagicSkillUse(this, this, curse.getId(), curse.getLevel(), 300, 0));
								curse.getEffects(this, this);
							}
							return;
						}
					}

					// Check if over-hit is possible
					if (skill.isOverhit() && target instanceof Attackable) {
						((Attackable) target).overhitEnabled(true);
					}
				}

				switch (skill.getSkillType()) {
					case COMMON_CRAFT: // Crafting does not trigger any chance skills.
					case DWARVEN_CRAFT:
						break;

					default: // Launch weapon Special ability skill effect if available
						if (getActiveWeaponItem() != null && !target.isDead()) {
							if (this instanceof Player && !getActiveWeaponItem().getSkillEffects(this, target, skill).isEmpty()) {
								sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_BEEN_ACTIVATED).addSkillName(skill));
							}
						}

						// Maybe launch chance skills on us
						if (_chanceSkills != null) {
							_chanceSkills.onSkillHit(target, false, skill.isMagic(), skill.isOffensive());
						}

						// Maybe launch chance skills on target
						if (target.getChanceSkills() != null) {
							target.getChanceSkills().onSkillHit(this, true, skill.isMagic(), skill.isOffensive());
						}
				}
			}

			// Launch the magic skill and calculate its effects
			final IHandler handler = HandlerTable.getInstance().get(skill.getSkillType());
			if (handler != null) {
				handler.invoke(this, skill, targets);
			} else {
				skill.useSkill(this, targets);
			}

			Player player = getPlayer();
			if (player != null) {
				for (WorldObject target : targets) {
					// EVT_ATTACKED and PvPStatus
					if (target instanceof Creature) {
						if (skill.isOffensive()) {
							if (target instanceof Playable) {
								// Signets are a special case, casted on target_self but don't harm self
								if (skill.getSkillType() != ESkillType.SIGNET && skill.getSkillType() != ESkillType.SIGNET_CASTTIME) {
									((Creature) target).getAI().clientStartAutoAttack();

									// attack of the own pet does not flag player
									if (player.getActiveSummon() != target) {
										player.updatePvPStatus((Creature) target);
									}
								}
							} // Add attacker into list
							else if (target instanceof Attackable && skill.getId() != 51) {
								((Attackable) target).addAttackerToAttackByList(this);
							}

							// notify target AI about the attack
							if (((Creature) target).hasAI()) {
								switch (skill.getSkillType()) {
									case AGGREDUCE:
									case AGGREDUCE_CHAR:
									case AGGREMOVE:
										break;

									default:
										((Creature) target).getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, this);
								}
							}
						} else {
							if (target instanceof Player) {
								// Casting non offensive skill on player with pvp flag set or with karma
								if (!(target.equals(this) || target.equals(player)) && (((Player) target).getPvpFlag() > 0 || ((Player) target).getKarma() > 0)) {
									player.updatePvPStatus();
								}
							} else if (target instanceof Attackable && !((Attackable) target).isGuard()) {
								switch (skill.getSkillType()) {
									case SUMMON:
									case BEAST_FEED:
									case UNLOCK:
									case UNLOCK_SPECIAL:
									case DELUXE_KEY_UNLOCK:
										break;

									default:
										player.updatePvPStatus();
								}
							}
						}

						switch (skill.getTargetType()) {
							case TARGET_CORPSE_MOB:
							case TARGET_AREA_CORPSE_MOB:
								if (((Creature) target).isDead()) {
									((Npc) target).endDecayTask();
								}
								break;
						}
					}
				}

				// Mobs in range 1000 see spell
				for (Npc npcMob : player.getKnownTypeInRadius(Npc.class, 1000)) {
					List<Quest> quests = npcMob.getTemplate().getEventQuests(EventType.ON_SKILL_SEE);
					if (quests != null) {
						for (Quest quest : quests) {
							quest.notifySkillSee(npcMob, player, skill, targets, this instanceof Summon);
						}
					}
				}
			}

			// Notify AI
			if (skill.isOffensive()) {
				switch (skill.getSkillType()) {
					case AGGREDUCE:
					case AGGREDUCE_CHAR:
					case AGGREMOVE:
						break;

					default:
						for (WorldObject target : targets) {
							// notify target AI about the attack
							if (target instanceof Creature && ((Creature) target).hasAI()) {
								((Creature) target).getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, this);
							}
						}
						break;
				}
			}
		} catch (Exception e) {
			_log.warn(getClass().getSimpleName() + ": callSkill() failed on skill id: " + skill.getId(), e);
		}
	}

	/**
	 * @param target Target to check.
	 * @return True if the Creature is behind the target and can't be seen.
	 */
	public boolean isBehind(Creature target) {
		if (target == null) {
			return false;
		}

		final double maxAngleDiff = 60;

		double angleChar = MathUtil.calculateAngleFrom(this, target);
		double angleTarget = MathUtil.convertHeadingToDegree(target.getHeading());
		double angleDiff = angleChar - angleTarget;

		if (angleDiff <= -360 + maxAngleDiff) {
			angleDiff += 360;
		}

		if (angleDiff >= 360 - maxAngleDiff) {
			angleDiff -= 360;
		}

		return Math.abs(angleDiff) <= maxAngleDiff;
	}

	public boolean isBehindTarget() {
		WorldObject target = getTarget();
		if (target instanceof Creature) {
			return isBehind((Creature) target);
		}

		return false;
	}

	/**
	 * @param target Target to check.
	 * @return True if the target is facing the Creature.
	 */
	public boolean isInFrontOf(Creature target) {
		if (target == null) {
			return false;
		}

		final double maxAngleDiff = 60;

		double angleTarget = MathUtil.calculateAngleFrom(target, this);
		double angleChar = MathUtil.convertHeadingToDegree(target.getHeading());
		double angleDiff = angleChar - angleTarget;

		if (angleDiff <= -360 + maxAngleDiff) {
			angleDiff += 360;
		}

		if (angleDiff >= 360 - maxAngleDiff) {
			angleDiff -= 360;
		}

		return Math.abs(angleDiff) <= maxAngleDiff;
	}

	/**
	 * @param target Target to check.
	 * @param maxAngle The angle to check.
	 * @return true if target is in front of Creature (shield def etc)
	 */
	public boolean isFacing(WorldObject target, int maxAngle) {
		if (target == null) {
			return false;
		}

		double maxAngleDiff = maxAngle / 2;
		double angleTarget = MathUtil.calculateAngleFrom(this, target);
		double angleChar = MathUtil.convertHeadingToDegree(getHeading());
		double angleDiff = angleChar - angleTarget;

		if (angleDiff <= -360 + maxAngleDiff) {
			angleDiff += 360;
		}

		if (angleDiff >= 360 - maxAngleDiff) {
			angleDiff -= 360;
		}

		return Math.abs(angleDiff) <= maxAngleDiff;
	}

	public boolean isInFrontOfTarget() {
		WorldObject target = getTarget();
		if (target instanceof Creature) {
			return isInFrontOf((Creature) target);
		}

		return false;
	}

	/**
	 * @return the level modifier.
	 */
	public double getLevelMod() {
		return (100.0 - 11 + getLevel()) / 100.0;
	}

	/**
	 * @param target Target to check.
	 * @return a Random Damage in function of the weapon.
	 */
	public final int getRandomDamage(Creature target) {
		Weapon weaponItem = getActiveWeaponItem();
		if (weaponItem == null) {
			return 5 + (int) Math.sqrt(getLevel());
		}

		return weaponItem.getRandomDamage();
	}

	@Override
	public String toString() {
		return "mob " + getObjectId();
	}

	/**
	 * @return the level of the Creature.
	 */
	public abstract int getLevel();

	// =========================================================
	// Stat - NEED TO REMOVE ONCE L2CHARSTAT IS COMPLETE
	// Property - Public
	public final double calcStat(Stats stat, double init, Creature target, L2Skill skill) {
		return getStat().calcStat(stat, init, target, skill);
	}

	// Property - Public
	public final int getCON() {
		return getStat().getCON();
	}

	public final int getDEX() {
		return getStat().getDEX();
	}

	public final int getINT() {
		return getStat().getINT();
	}

	public final int getMEN() {
		return getStat().getMEN();
	}

	public final int getSTR() {
		return getStat().getSTR();
	}

	public final int getWIT() {
		return getStat().getWIT();
	}

	public final int getAccuracy() {
		return getStat().getAccuracy();
	}

	public final int getCriticalHit(Creature target, L2Skill skill) {
		return getStat().getCriticalHit(target, skill);
	}

	public final int getEvasionRate(Creature target) {
		return getStat().getEvasionRate(target);
	}

	public final int getMDef(Creature target, L2Skill skill) {
		return getStat().getMDef(target, skill);
	}

	public final int getPDef(Creature target) {
		return getStat().getPDef(target);
	}

	public final int getShldDef() {
		return getStat().getShldDef();
	}

	public final int getPhysicalAttackRange() {
		return getStat().getPhysicalAttackRange();
	}

	public final int getPAtk(Creature target) {
		return getStat().getPAtk(target);
	}

	public final int getPAtkSpd() {
		return getStat().getPAtkSpd();
	}

	public final int getMAtk(Creature target, L2Skill skill) {
		return getStat().getMAtk(target, skill);
	}

	public final int getMAtkSpd() {
		return getStat().getMAtkSpd();
	}

	public final int getMCriticalHit(Creature target, L2Skill skill) {
		return getStat().getMCriticalHit(target, skill);
	}

	public final int getMaxMp() {
		return getStat().getMaxMp();
	}

	public int getMaxHp() {
		return getStat().getMaxHp();
	}

	public final int getMaxCp() {
		return getStat().getMaxCp();
	}

	public final double getPAtkAnimals(Creature target) {
		return getStat().getPAtkAnimals(target);
	}

	public final double getPAtkDragons(Creature target) {
		return getStat().getPAtkDragons(target);
	}

	public final double getPAtkInsects(Creature target) {
		return getStat().getPAtkInsects(target);
	}

	public final double getPAtkMonsters(Creature target) {
		return getStat().getPAtkMonsters(target);
	}

	public final double getPAtkUndeads(Creature target) {
		return getStat().getPAtkUndeads(target);
	}

	public final double getPAtkPlants(Creature target) {
		return getStat().getPAtkPlants(target);
	}

	public final double getPAtkGiants(Creature target) {
		return getStat().getPAtkGiants(target);
	}

	public final double getPAtkMagicCreatures(Creature target) {
		return getStat().getPAtkMagicCreatures(target);
	}

	public final double getPAtkDemons(Creature target) {
		return getStat().getPAtkDemons(target);
	}

	public final double getPDefAnimals(Creature target) {
		return getStat().getPDefAnimals(target);
	}

	public final double getPDefDragons(Creature target) {
		return getStat().getPDefDragons(target);
	}

	public final double getPDefInsects(Creature target) {
		return getStat().getPDefInsects(target);
	}

	public final double getPDefMonsters(Creature target) {
		return getStat().getPDefMonsters(target);
	}

	public final double getPDefPlants(Creature target) {
		return getStat().getPDefPlants(target);
	}

	public final double getPDefGiants(Creature target) {
		return getStat().getPDefGiants(target);
	}

	public final double getPDefMagicCreatures(Creature target) {
		return getStat().getPDefMagicCreatures(target);
	}

	public final double getPDefDemons(Creature target) {
		return getStat().getPDefDemons(target);
	}

	public final int getMoveSpeed() {
		return (int) getStat().getMoveSpeed();
	}

	// =========================================================
	// Status - NEED TO REMOVE ONCE L2CHARTATUS IS COMPLETE
	// Method - Public
	public void addStatusListener(Creature object) {
		getStatus().addStatusListener(object);
	}

	public void reduceCurrentHp(double i, Creature attacker, L2Skill skill) {
		reduceCurrentHp(i, attacker, true, false, skill);
	}

	public void reduceCurrentHpByDOT(double i, Creature attacker, L2Skill skill) {
		reduceCurrentHp(i, attacker, !skill.isToggle(), true, skill);
	}

	public void reduceCurrentHp(double damage, Creature attacker, boolean awake, boolean isDOT, L2Skill skill) {
		if (isChampion() && Config.CHAMPION_HP != 0) {
			getStatus().reduceHp(damage / Config.CHAMPION_HP, attacker, awake, isDOT, false);
		} else {
			getStatus().reduceHp(damage, attacker, awake, isDOT, false);
		}
		eventBus.notify(new OnReduceHp(this, attacker, skill, damage, getCurrentHp()));
	}

	public void reduceCurrentMp(double i) {
		getStatus().reduceMp(i);
	}

	public void removeStatusListener(Creature object) {
		getStatus().removeStatusListener(object);
	}

	// Property - Public
	public final double getCurrentCp() {
		return getStatus().getCurrentCp();
	}

	public final void setCurrentCp(double newCp) {
		getStatus().setCurrentCp(newCp);
	}

	public final double getCurrentHp() {
		return getStatus().getCurrentHp();
	}

	public final void setCurrentHp(double newHp) {
		getStatus().setCurrentHp(newHp);
	}

	public final void setCurrentHpMp(double newHp, double newMp) {
		getStatus().setCurrentHpMp(newHp, newMp);
	}

	public final double getCurrentMp() {
		return getStatus().getCurrentMp();
	}

	public final void setCurrentMp(double newMp) {
		getStatus().setCurrentMp(newMp);
	}
	
	public void setFullHpMpCp() {
		setCurrentHpMp(getMaxHp(), getMaxMp());
	}

	// =========================================================
	public void setChampion(boolean champ) {
		_champion = champ;
	}

	public boolean isChampion() {
		return _champion;
	}

	/**
	 * Send system message about damage.<BR>
	 * <BR>
	 * <B><U> Overriden in </U> :</B>
	 * <ul>
	 * <li>Player</li>
	 * <li>Servitor</li>
	 * <li>Pet</li>
	 * </ul>
	 *
	 * @param target
	 * @param damage
	 * @param mcrit
	 * @param pcrit
	 * @param miss
	 */
	public void sendDamageMessage(Creature target, int damage, boolean mcrit, boolean pcrit, boolean miss, boolean parry) {
	}

	public FusionSkill getFusionSkill() {
		return _fusionSkill;
	}

	public void setFusionSkill(FusionSkill fb) {
		_fusionSkill = fb;
	}

	public int getAttackElementValue(byte attackAttribute) {
		return getStat().getAttackElementValue(attackAttribute);
	}

	public double getDefenseElementValue(byte defenseAttribute) {
		return getStat().getDefenseElementValue(defenseAttribute);
	}

	/**
	 * Check if target is affected with special buff
	 *
	 * @see CharEffectList#isAffected(L2EffectFlag)
	 * @param flag int
	 * @return boolean
	 */
	public boolean isAffected(EEffectFlag flag) {
		return _effects.isAffected(flag);
	}

	/**
	 * Check player max buff count
	 *
	 * @return max buff count
	 */
	public int getMaxBuffCount() {
		return Config.BUFFS_MAX_AMOUNT + Math.max(0, getSkillLevel(L2Skill.SKILL_DIVINE_INSPIRATION));
	}

	/**
	 * @return a multiplier based on weapon random damage.
	 */
	public final double getRandomDamageMultiplier() {
		Weapon activeWeapon = getActiveWeaponItem();
		int random;

		if (activeWeapon != null) {
			random = activeWeapon.getRandomDamage();
		} else {
			random = 5 + (int) Math.sqrt(getLevel());
		}

		return (1 + ((double) Rnd.get(0 - random, random) / 100));
	}

	public void disableCoreAI(boolean val) {
		_AIdisabled = val;
	}

	public boolean isCoreAIDisabled() {
		return _AIdisabled;
	}

	/**
	 * @return true if the character is located in an arena (aka a PvP zone
	 * which isn't a siege).
	 */
	public boolean isInArena() {
		return false;
	}

	public double getCollisionRadius() {
		return getTemplate().getCollisionRadius();
	}

	public double getCollisionHeight() {
		return getTemplate().getCollisionHeight();
	}

	@Override
	public final void setRegion(WorldRegion value) {
		// confirm revalidation of old region's zones
		if (getRegion() != null) {
			if (value != null) {
				getRegion().revalidateZones(this);
			} else {
				getRegion().removeFromZones(this);
			}
		}

		super.setRegion(value);
	}

	@Override
	public void removeKnownObject(WorldObject object) {
		// If object is targeted by the Creature, cancel Attack or Cast
		if (object == getTarget()) {
			setTarget(null);
		}
	}

	@Override
	public boolean isCreature() {
		return true;
	}

	@Override
	public Creature getCreature() {
		return this;
	}

	public boolean isInSameParty(Creature target) {
		return false;
	}

	public boolean isInSameClan(Creature target) {
		return false;
	}

	public boolean isInSameAlly(Creature target) {
		return false;
	}

	public void removeTarget() {
		abortAttack();
		abortCast();
		setTarget(null);
		getAI().setIntention(CtrlIntention.IDLE);
	}
}
