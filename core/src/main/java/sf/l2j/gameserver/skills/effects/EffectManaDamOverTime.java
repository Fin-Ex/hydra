package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("ManaDamOverTime")
public class EffectManaDamOverTime extends L2Effect {

	public EffectManaDamOverTime(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.MANA_DMG_OVER_TIME;
	}

	@Override
	public boolean onActionTime() {
		if (getEffected().isDead()) {
			return false;
		}

		double manaDam = calc();

		if (manaDam > getEffected().getCurrentMp()) {
			if (getSkill().isToggle()) {
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP);
				getEffected().sendPacket(sm);
				return false;
			}
		}

		getEffected().reduceCurrentMp(manaDam);
		return true;
	}
}