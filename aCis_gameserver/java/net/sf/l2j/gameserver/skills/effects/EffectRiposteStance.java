package net.sf.l2j.gameserver.skills.effects;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;

@Effect("RiposteStance")
public class EffectRiposteStance extends EffectManaDamOverTime {

	private final Player caster;

	public EffectRiposteStance(Env env, EffectTemplate template) {
		super(env, template);
		caster = env.getPlayer();
	}

	@Override
	public boolean onActionTime() {
		if (caster.hasTalent(SkillTable.FrequentTalent.DUAL_SWORD_MASTERY)) {
			return true;
		}
		return super.onActionTime();
	}
}
