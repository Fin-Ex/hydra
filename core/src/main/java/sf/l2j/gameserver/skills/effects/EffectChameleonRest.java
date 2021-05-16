package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.ESkillType;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("ChameleonRest")
public class EffectChameleonRest extends L2Effect {

	public EffectChameleonRest(Env env, EffectTemplate template) {
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

		// Only cont skills shouldn't end
		if (getSkill().getSkillType() != ESkillType.CONT) {
			return false;
		}

		if (getEffected() instanceof Player) {
			if (!((Player) getEffected()).isSitting()) {
				return false;
			}
		}

		double manaDam = calc();

		if (manaDam > getEffected().getCurrentMp()) {
			getEffected().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP));
			return false;
		}

		getEffected().reduceCurrentMp(manaDam);
		return true;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.SILENT_MOVE.getMask() | EEffectFlag.RELAXING.getMask();
	}
}
