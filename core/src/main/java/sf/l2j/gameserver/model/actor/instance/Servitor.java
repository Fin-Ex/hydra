package sf.l2j.gameserver.model.actor.instance;

import java.util.concurrent.Future;

import sf.finex.handlers.dialog.DlgManager;
import sf.finex.handlers.dialog.requests.ReviveRequest;
import sf.l2j.commons.concurrent.ThreadPool;
import sf.l2j.gameserver.model.AggroInfo;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Attackable;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.Summon;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.network.serverpackets.SetSummonRemainTime;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.skills.l2skills.L2SkillSummon;
import sf.l2j.gameserver.taskmanager.DecayTaskManager;

public class Servitor extends Summon {

	private float _expPenalty = 0;
	private int _itemConsumeId = 0;
	private int _itemConsumeCount = 0;
	private int _itemConsumeSteps = 0;
	private int _totalLifeTime = 1200000;
	private int _timeLostIdle = 1000;
	private int _timeLostActive = 1000;
	private int _timeRemaining;
	private int _nextItemConsumeTime;

	public int lastShowntimeRemaining;

	private Future<?> _summonLifeTask;

	public Servitor(int objectId, NpcTemplate template, Player owner, L2Skill skill) {
		super(objectId, template, owner);

		if (skill != null) {
			final L2SkillSummon summonSkill = (L2SkillSummon) skill;
			_itemConsumeId = summonSkill.getItemConsumeIdOT();
			_itemConsumeCount = summonSkill.getItemConsumeOT();
			_itemConsumeSteps = summonSkill.getItemConsumeSteps();
			_totalLifeTime = summonSkill.getTotalLifeTime();
			_timeLostIdle = summonSkill.getTimeLostIdle();
			_timeLostActive = summonSkill.getTimeLostActive();
		}
		_timeRemaining = _totalLifeTime;
		lastShowntimeRemaining = _totalLifeTime;

		if (_itemConsumeId == 0 || _itemConsumeSteps == 0) {
			_nextItemConsumeTime = -1; // do not consume
		} else {
			_nextItemConsumeTime = _totalLifeTime - _totalLifeTime / (_itemConsumeSteps + 1);
		}

		_summonLifeTask = ThreadPool.scheduleAtFixedRate(new SummonLifetime(getPlayer(), this), 1000, 1000);
	}

	@Override
	public final int getLevel() {
		return (getTemplate() != null ? getTemplate().getLevel() : 0);
	}

	@Override
	public int getSummonType() {
		return 1;
	}

	public void setExpPenalty(float expPenalty) {
		_expPenalty = expPenalty;
	}

	public float getExpPenalty() {
		return _expPenalty;
	}

	public int getItemConsumeCount() {
		return _itemConsumeCount;
	}

	public int getItemConsumeId() {
		return _itemConsumeId;
	}

	public int getItemConsumeSteps() {
		return _itemConsumeSteps;
	}

	public int getNextItemConsumeTime() {
		return _nextItemConsumeTime;
	}

	public int getTotalLifeTime() {
		return _totalLifeTime;
	}

	public int getTimeLostIdle() {
		return _timeLostIdle;
	}

	public int getTimeLostActive() {
		return _timeLostActive;
	}

	public int getTimeRemaining() {
		return _timeRemaining;
	}

	public void setNextItemConsumeTime(int value) {
		_nextItemConsumeTime = value;
	}

	public void decNextItemConsumeTime(int value) {
		_nextItemConsumeTime -= value;
	}

	public void decTimeRemaining(int value) {
		_timeRemaining -= value;
	}

	public void addExpAndSp(int addToExp, int addToSp) {
		getPlayer().addExpAndSp(addToExp, addToSp);
	}

	@Override
	public boolean doDie(Creature killer) {
		if (!super.doDie(killer)) {
			return false;
		}

		// Send aggro of mobs to summoner.
		for (Attackable mob : getKnownType(Attackable.class)) {
			if (mob.isDead()) {
				continue;
			}

			final AggroInfo info = mob.getAggroList().get(this);
			if (info != null) {
				mob.addDamageHate(getPlayer(), info.getDamage(), info.getHate());
			}
		}

		// Popup for summon if phoenix buff was on
		if (isPhoenixBlessed()) {
			DlgManager.getInstance().getRequest(ReviveRequest.class).handle(getPlayer(), getPlayer(), null, true);
		}

		DecayTaskManager.getInstance().add(this, getTemplate().getCorpseTime());

		if (_summonLifeTask != null) {
			_summonLifeTask.cancel(false);
			_summonLifeTask = null;
		}
		return true;

	}

	static class SummonLifetime implements Runnable {

		private final Player _activeChar;
		private final Servitor _summon;

		SummonLifetime(Player activeChar, Servitor newpet) {
			_activeChar = activeChar;
			_summon = newpet;
		}

		@Override
		public void run() {
			try {
				double oldTimeRemaining = _summon.getTimeRemaining();
				int maxTime = _summon.getTotalLifeTime();
				double newTimeRemaining;

				// if pet is attacking
				if (_summon.isAttackingNow()) {
					_summon.decTimeRemaining(_summon.getTimeLostActive());
				} else {
					_summon.decTimeRemaining(_summon.getTimeLostIdle());
				}

				newTimeRemaining = _summon.getTimeRemaining();

				// check if the summon's lifetime has ran out
				if (newTimeRemaining < 0) {
					_summon.unSummon(_activeChar);
				} else if ((newTimeRemaining <= _summon.getNextItemConsumeTime()) && (oldTimeRemaining > _summon.getNextItemConsumeTime())) {
					_summon.decNextItemConsumeTime(maxTime / (_summon.getItemConsumeSteps() + 1));

					// check if owner has enought itemConsume, if requested
					if (_summon.getItemConsumeCount() > 0 && _summon.getItemConsumeId() != 0 && !_summon.isDead() && !_summon.destroyItemByItemId("Consume", _summon.getItemConsumeId(), _summon.getItemConsumeCount(), _activeChar, true)) {
						_summon.unSummon(_activeChar);
					}
				}

				// prevent useless packet-sending when the difference isn't visible.
				if ((_summon.lastShowntimeRemaining - newTimeRemaining) > maxTime / 352) {
					_activeChar.sendPacket(new SetSummonRemainTime(maxTime, (int) newTimeRemaining));
					_summon.lastShowntimeRemaining = (int) newTimeRemaining;
					_summon.updateEffectIcons();
				}
			} catch (Exception e) {
				_log.error("Error on player [" + _activeChar.getName() + "] summon item consume task.", e);
			}
		}
	}

	@Override
	public void unSummon(Player owner) {
		if (_summonLifeTask != null) {
			_summonLifeTask.cancel(false);
			_summonLifeTask = null;
		}
		super.unSummon(owner);
	}

	@Override
	public boolean destroyItem(String process, int objectId, int count, WorldObject reference, boolean sendMessage) {
		return getPlayer().destroyItem(process, objectId, count, reference, sendMessage);
	}

	@Override
	public boolean destroyItemByItemId(String process, int itemId, int count, WorldObject reference, boolean sendMessage) {
		return getPlayer().destroyItemByItemId(process, itemId, count, reference, sendMessage);
	}

	@Override
	public void doPickupItem(WorldObject object) {
	}

	@Override
	public boolean isServitor() {
		return true;
	}

	@Override
	public Servitor getServitor() {
		return this;
	}

}
