package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.Player;
import java.util.concurrent.Future;

import sf.l2j.commons.concurrent.ThreadPool;

import sf.l2j.gameserver.data.SkillTable;
import sf.l2j.gameserver.instancemanager.FourSepulchersManager;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.model.group.Party;
import sf.l2j.gameserver.network.serverpackets.NpcSay;
import sf.l2j.gameserver.scripting.QuestState;
import sf.l2j.gameserver.skills.L2Skill;

public class SepulcherMonster extends Monster {

	private static final String QUEST_ID = "Q620_FourGoblets";

	public int mysteriousBoxId = 0;

	protected Future<?> _victimSpawnKeyBoxTask = null;
	protected Future<?> _victimShout = null;
	protected Future<?> _changeImmortalTask = null;
	protected Future<?> _onDeadEventTask = null;

	public SepulcherMonster(int objectId, NpcTemplate template) {
		super(objectId, template);
		setShowSummonAnimation(true);

		switch (template.getNpcId()) {
			case 25339:
			case 25342:
			case 25346:
			case 25349:
				setIsRaid(true);
		}
	}

	@Override
	public void onSpawn() {
		setShowSummonAnimation(false);
		switch (getNpcId()) {
			case 18150:
			case 18151:
			case 18152:
			case 18153:
			case 18154:
			case 18155:
			case 18156:
			case 18157:
				if (_victimSpawnKeyBoxTask != null) {
					_victimSpawnKeyBoxTask.cancel(true);
				}
				_victimSpawnKeyBoxTask = ThreadPool.schedule(new VictimSpawnKeyBox(this), 300000);
				if (_victimShout != null) {
					_victimShout.cancel(true);
				}
				_victimShout = ThreadPool.schedule(new VictimShout(this), 5000);
				break;

			case 18196:
			case 18197:
			case 18198:
			case 18199:
			case 18200:
			case 18201:
			case 18202:
			case 18203:
			case 18204:
			case 18205:
			case 18206:
			case 18207:
			case 18208:
			case 18209:
			case 18210:
			case 18211:
				break;

			case 18231:
			case 18232:
			case 18233:
			case 18234:
			case 18235:
			case 18236:
			case 18237:
			case 18238:
			case 18239:
			case 18240:
			case 18241:
			case 18242:
			case 18243:
				if (_changeImmortalTask != null) {
					_changeImmortalTask.cancel(true);
				}
				_changeImmortalTask = ThreadPool.schedule(new ChangeImmortal(this), 1600);
				break;

			case 18256:
				break;

			case 25339:
			case 25342:
			case 25346:
			case 25349:
				setIsRaid(true);
				break;
		}
		super.onSpawn();
	}

	@Override
	public boolean doDie(Creature killer) {
		if (!super.doDie(killer)) {
			return false;
		}

		switch (getNpcId()) {
			case 18120:
			case 18121:
			case 18122:
			case 18123:
			case 18124:
			case 18125:
			case 18126:
			case 18127:
			case 18128:
			case 18129:
			case 18130:
			case 18131:
			case 18149:
			case 18158:
			case 18159:
			case 18160:
			case 18161:
			case 18162:
			case 18163:
			case 18164:
			case 18165:
			case 18183:
			case 18184:
			case 18212:
			case 18213:
			case 18214:
			case 18215:
			case 18216:
			case 18217:
			case 18218:
			case 18219:
				if (_onDeadEventTask != null) {
					_onDeadEventTask.cancel(true);
				}
				_onDeadEventTask = ThreadPool.schedule(new OnDeadEvent(this), 3500);
				break;

			case 18150:
			case 18151:
			case 18152:
			case 18153:
			case 18154:
			case 18155:
			case 18156:
			case 18157:
				if (_victimSpawnKeyBoxTask != null) {
					_victimSpawnKeyBoxTask.cancel(true);
					_victimSpawnKeyBoxTask = null;
				}
				if (_victimShout != null) {
					_victimShout.cancel(true);
					_victimShout = null;
				}
				if (_onDeadEventTask != null) {
					_onDeadEventTask.cancel(true);
				}
				_onDeadEventTask = ThreadPool.schedule(new OnDeadEvent(this), 3500);
				break;

			case 18141:
			case 18142:
			case 18143:
			case 18144:
			case 18145:
			case 18146:
			case 18147:
			case 18148:
				if (FourSepulchersManager.getInstance().isViscountMobsAnnihilated(mysteriousBoxId)) {
					if (_onDeadEventTask != null) {
						_onDeadEventTask.cancel(true);
					}
					_onDeadEventTask = ThreadPool.schedule(new OnDeadEvent(this), 3500);
				}
				break;

			case 18220:
			case 18221:
			case 18222:
			case 18223:
			case 18224:
			case 18225:
			case 18226:
			case 18227:
			case 18228:
			case 18229:
			case 18230:
			case 18231:
			case 18232:
			case 18233:
			case 18234:
			case 18235:
			case 18236:
			case 18237:
			case 18238:
			case 18239:
			case 18240:
				if (FourSepulchersManager.getInstance().isDukeMobsAnnihilated(mysteriousBoxId)) {
					if (_onDeadEventTask != null) {
						_onDeadEventTask.cancel(true);
					}
					_onDeadEventTask = ThreadPool.schedule(new OnDeadEvent(this), 3500);
				}
				break;

			case 25339:
			case 25342:
			case 25346:
			case 25349:
				giveCup(killer);
				if (_onDeadEventTask != null) {
					_onDeadEventTask.cancel(true);
				}
				_onDeadEventTask = ThreadPool.schedule(new OnDeadEvent(this), 8500);
				break;
		}
		return true;
	}

	@Override
	public void deleteMe() {
		if (_victimSpawnKeyBoxTask != null) {
			_victimSpawnKeyBoxTask.cancel(true);
			_victimSpawnKeyBoxTask = null;
		}
		if (_onDeadEventTask != null) {
			_onDeadEventTask.cancel(true);
			_onDeadEventTask = null;
		}
		super.deleteMe();
	}

	private void giveCup(Creature killer) {
		int cupId = 0;

		switch (getNpcId()) {
			case 25339:
				cupId = 7256;
				break;
			case 25342:
				cupId = 7257;
				break;
			case 25346:
				cupId = 7258;
				break;
			case 25349:
				cupId = 7259;
				break;
		}

		final Player player = killer.getPlayer();
		if (player == null) {
			return;
		}

		final Party party = player.getParty();
		if (party != null) {
			for (Player member : party.getMembers()) {
				final QuestState qs = member.getQuestState(QUEST_ID);
				if (qs != null && (qs.isStarted() || qs.isCompleted()) && member.getInventory().getItemByItemId(7262) == null) {
					member.addItem("Quest", cupId, 1, member, true);
				}
			}
		} else {
			final QuestState qs = player.getQuestState(QUEST_ID);
			if (qs != null && (qs.isStarted() || qs.isCompleted()) && player.getInventory().getItemByItemId(7262) == null) {
				player.addItem("Quest", cupId, 1, player, true);
			}
		}
	}

	private class VictimShout implements Runnable {

		private final SepulcherMonster _activeChar;

		public VictimShout(SepulcherMonster activeChar) {
			_activeChar = activeChar;
		}

		@Override
		public void run() {
			if (_activeChar.isDead()) {
				return;
			}

			if (!_activeChar.isVisible()) {
				return;
			}

			broadcastPacket(new NpcSay(getObjectId(), 0, getNpcId(), "forgive me!!"));
		}
	}

	private class VictimSpawnKeyBox implements Runnable {

		private final SepulcherMonster _activeChar;

		public VictimSpawnKeyBox(SepulcherMonster activeChar) {
			_activeChar = activeChar;
		}

		@Override
		public void run() {
			if (_activeChar.isDead()) {
				return;
			}

			if (!_activeChar.isVisible()) {
				return;
			}

			FourSepulchersManager.getInstance().spawnKeyBox(_activeChar);
			broadcastPacket(new NpcSay(getObjectId(), 0, getNpcId(), "Many thanks for rescue me."));
			if (_victimShout != null) {
				_victimShout.cancel(true);
			}
		}
	}

	private static class OnDeadEvent implements Runnable {

		SepulcherMonster _activeChar;

		public OnDeadEvent(SepulcherMonster activeChar) {
			_activeChar = activeChar;
		}

		@Override
		public void run() {
			switch (_activeChar.getNpcId()) {
				case 18120:
				case 18121:
				case 18122:
				case 18123:
				case 18124:
				case 18125:
				case 18126:
				case 18127:
				case 18128:
				case 18129:
				case 18130:
				case 18131:
				case 18149:
				case 18158:
				case 18159:
				case 18160:
				case 18161:
				case 18162:
				case 18163:
				case 18164:
				case 18165:
				case 18183:
				case 18184:
				case 18212:
				case 18213:
				case 18214:
				case 18215:
				case 18216:
				case 18217:
				case 18218:
				case 18219:
					FourSepulchersManager.getInstance().spawnKeyBox(_activeChar);
					break;

				case 18150:
				case 18151:
				case 18152:
				case 18153:
				case 18154:
				case 18155:
				case 18156:
				case 18157:
					FourSepulchersManager.getInstance().spawnExecutionerOfHalisha(_activeChar);
					break;

				case 18141:
				case 18142:
				case 18143:
				case 18144:
				case 18145:
				case 18146:
				case 18147:
				case 18148:
					FourSepulchersManager.getInstance().spawnMonster(_activeChar.mysteriousBoxId);
					break;

				case 18220:
				case 18221:
				case 18222:
				case 18223:
				case 18224:
				case 18225:
				case 18226:
				case 18227:
				case 18228:
				case 18229:
				case 18230:
				case 18231:
				case 18232:
				case 18233:
				case 18234:
				case 18235:
				case 18236:
				case 18237:
				case 18238:
				case 18239:
				case 18240:
					FourSepulchersManager.getInstance().spawnArchonOfHalisha(_activeChar.mysteriousBoxId);
					break;

				case 25339:
				case 25342:
				case 25346:
				case 25349:
					FourSepulchersManager.getInstance().spawnEmperorsGraveNpc(_activeChar.mysteriousBoxId);
					break;
			}
		}
	}

	private static class ChangeImmortal implements Runnable {

		SepulcherMonster activeChar;

		public ChangeImmortal(SepulcherMonster mob) {
			activeChar = mob;
		}

		@Override
		public void run() {
			L2Skill fp = SkillTable.FrequentSkill.FAKE_PETRIFICATION.getSkill(); // Invulnerable by petrification
			fp.getEffects(activeChar, activeChar);
		}
	}

	@Override
	public boolean isAutoAttackable(Creature attacker) {
		return true;
	}
}
