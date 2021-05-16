package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.network.serverpackets.ExRegenMax;
import sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("CombatPointHealOverTime")
public class EffectCombatPointHealOverTime extends L2Effect {

	public EffectCombatPointHealOverTime(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.COMBAT_POINT_HEAL_OVER_TIME;
	}

	@Override
	public boolean onStart() {
		// If effected is a player, send a hp regen effect packet.
		if (getEffected().isPlayer() && getTotalCount() > 0 && getPeriod() > 0) {
			getEffected().sendPacket(new ExRegenMax(getTotalCount() * getPeriod(), getPeriod(), calc()));
		}

		return true;
	}

	@Override
	public boolean onActionTime() {
		if (getEffected().isDead()) {
			return false;
		}

		double cp = getEffected().getCurrentCp();
		double maxcp = getEffected().getMaxCp();
		cp += calc();

		if (cp > maxcp) {
			cp = maxcp;
		}

		getEffected().setCurrentCp(cp);
		StatusUpdate sump = new StatusUpdate(getEffected());
		sump.addAttribute(StatusUpdate.CUR_CP, (int) cp);
		getEffected().sendPacket(sump);
		return true;
	}
}
