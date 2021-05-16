package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("Relax")
public class EffectRelax extends L2Effect {

	public EffectRelax(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.RELAXING;
	}

	@Override
	public boolean onStart() {
		if (getEffected() instanceof Player) {
			((Player) getEffected()).sitDown(false);
		} else {
			getEffected().getAI().setIntention(CtrlIntention.REST);
		}

		return super.onStart();
	}

	@Override
	public void onExit() {
		super.onExit();
	}

	@Override
	public boolean onActionTime() {
		if (getEffected().isDead()) {
			return false;
		}

		if (getEffected() instanceof Player) {
			if (!((Player) getEffected()).isSitting()) {
				return false;
			}
		}

		if (getEffected().getCurrentHp() + 1 > getEffected().getMaxHp()) {
			if (getSkill().isToggle()) {
				getEffected().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SKILL_DEACTIVATED_HP_FULL));
				return false;
			}
		}

		double manaDam = calc();

		if (manaDam > getEffected().getCurrentMp()) {
			if (getSkill().isToggle()) {
				getEffected().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP));
				return false;
			}
		}

		getEffected().reduceCurrentMp(manaDam);
		return true;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.RELAXING.getMask();
	}
}
