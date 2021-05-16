package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("ManaHealOverTime")
public class EffectManaHealOverTime extends L2Effect {

	public EffectManaHealOverTime(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.MANA_HEAL_OVER_TIME;
	}

	@Override
	public boolean onActionTime() {
		if (getEffected().isDead()) {
			return false;
		}

		double mp = getEffected().getCurrentMp();
		double maxmp = getEffected().getMaxMp();
		mp += calc();

		if (mp > maxmp) {
			mp = maxmp;
		}

		getEffected().setCurrentMp(mp);
		StatusUpdate sump = new StatusUpdate(getEffected());
		sump.addAttribute(StatusUpdate.CUR_MP, (int) mp);
		getEffected().sendPacket(sump);
		return true;
	}
}
