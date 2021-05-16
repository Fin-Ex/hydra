package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.ESkillType;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("SilentMove")
public class EffectSilentMove extends L2Effect {

	public EffectSilentMove(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public boolean onStart() {
		super.onStart();
		return true;
	}

	@Override
	public void onExit() {
		super.onExit();
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.SILENT_MOVE;
	}

	@Override
	public boolean onActionTime() {
		// Only cont skills shouldn't end
		if (getSkill().getSkillType() != ESkillType.CONT) {
			return false;
		}

		if (getEffected().isDead()) {
			return false;
		}

		double manaDam = calc();

		if (manaDam > getEffected().getCurrentMp()) {
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP);
			getEffected().sendPacket(sm);
			return false;
		}

		getEffected().reduceCurrentMp(manaDam);
		return true;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.SILENT_MOVE.getMask();
	}
}
