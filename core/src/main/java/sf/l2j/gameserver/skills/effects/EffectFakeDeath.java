package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author mkizub
 */
@Effect("FakeDeath")
public class EffectFakeDeath extends L2Effect {

	public EffectFakeDeath(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.FAKE_DEATH;
	}

	@Override
	public boolean onStart() {
		getEffected().startFakeDeath();
		return true;
	}

	@Override
	public void onExit() {
		getEffected().stopFakeDeath(false);
	}

	@Override
	public boolean onActionTime() {
		if (getEffected().isDead()) {
			return false;
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
}
