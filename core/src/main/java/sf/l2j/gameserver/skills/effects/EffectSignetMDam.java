package sf.l2j.gameserver.skills.effects;

import java.util.ArrayList;
import java.util.List;

import sf.finex.enums.ESkillTargetType;
import sf.l2j.gameserver.data.NpcTable;
import sf.l2j.gameserver.idfactory.IdFactory;
import sf.l2j.gameserver.model.ShotType;
import sf.l2j.gameserver.model.actor.Attackable;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.Summon;
import sf.l2j.gameserver.model.actor.ai.CtrlEvent;
import sf.l2j.gameserver.model.actor.instance.EffectPoint;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.model.location.Location;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.MagicSkillLaunched;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.skills.l2skills.L2SkillSignetCasttime;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("SignetMDam")
public class EffectSignetMDam extends L2Effect {

	private EffectPoint _actor;

	public EffectSignetMDam(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.SIGNET_GROUND;
	}

	@Override
	public boolean onStart() {
		NpcTemplate template;
		if (getSkill() instanceof L2SkillSignetCasttime) {
			template = NpcTable.getInstance().getTemplate(((L2SkillSignetCasttime) getSkill())._effectNpcId);
		} else {
			return false;
		}

		EffectPoint effectPoint = new EffectPoint(IdFactory.getInstance().getNextId(), template, getEffector());
		effectPoint.setCurrentHp(effectPoint.getMaxHp());
		effectPoint.setCurrentMp(effectPoint.getMaxMp());

		int x = getEffector().getX();
		int y = getEffector().getY();
		int z = getEffector().getZ();

		if (getEffector() instanceof Player && getSkill().getTargetType() == ESkillTargetType.TARGET_GROUND) {
			Location wordPosition = ((Player) getEffector()).getCurrentSkillWorldPosition();

			if (wordPosition != null) {
				x = wordPosition.getX();
				y = wordPosition.getY();
				z = wordPosition.getZ();
			}
		}
		effectPoint.setIsInvul(true);
		effectPoint.spawnMe(x, y, z);

		_actor = effectPoint;
		return true;

	}

	@Override
	public boolean onActionTime() {
		if (getCount() >= getTotalCount() - 2) {
			return true; // do nothing first 2 times
		}
		final Player caster = (Player) getEffector();

		final int mpConsume = getSkill().getMpConsume();

		final boolean sps = caster.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = caster.isChargedShot(ShotType.BLESSED_SPIRITSHOT);

		List<Creature> targets = new ArrayList<>();

		for (Creature cha : _actor.getKnownTypeInRadius(Creature.class, getSkill().getSkillRadius())) {
			if (cha == caster) {
				continue;
			}

			if (cha instanceof Attackable || cha instanceof Playable) {
				if (cha.isAlikeDead()) {
					continue;
				}

				if (mpConsume > caster.getCurrentMp()) {
					caster.sendPacket(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP);
					return false;
				}

				caster.reduceCurrentMp(mpConsume);

				if (cha instanceof Playable) {
					if (caster.canAttackCharacter(cha)) {
						targets.add(cha);
						caster.updatePvPStatus(cha);
					}
				} else {
					targets.add(cha);
				}
			}
		}

		if (!targets.isEmpty()) {
			caster.broadcastPacket(new MagicSkillLaunched(caster, getSkill().getId(), getSkill().getLevel(), targets.toArray(new Creature[targets.size()])));
			for (Creature target : targets) {
				boolean mcrit = Formulas.calcMCrit(caster.getMCriticalHit(target, getSkill()));
				boolean parry = Formulas.calcParry(caster, target, getSkill());
				byte shld = Formulas.calcShldUse(caster, target, getSkill());

				int mdam = (int) Formulas.calcMagicDam(caster, target, getSkill(), shld, parry, sps, bsps, mcrit);

				if (target instanceof Summon) {
					target.broadcastStatusUpdate();
				}

				if (mdam > 0) {
					// Manage cast break of the target (calculating rate, sending message...)
					Formulas.calcCastBreak(target, mdam);

					caster.sendDamageMessage(target, mdam, mcrit, false, false, parry);
					target.reduceCurrentHp(mdam, caster, getSkill());
				}
				target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, caster);
			}
		}
		return true;
	}

	@Override
	public void onExit() {
		if (_actor != null) {
			_actor.deleteMe();
		}
	}
}
