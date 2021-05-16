/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.AbnormalEffect;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.Stats;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 *
 * @author FinFan
 */
@Effect("Bleed")
public class EffectBleed extends EffectDamOverTime {

	public EffectBleed(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BLEED;
	}

	@Override
	public boolean onStart() {
		getEffected().startAbnormalEffect(AbnormalEffect.BLEEDING);
		return true;
	}

	@Override
	public boolean onActionTime() {
		damage = getEffector().calcStat(Stats.Bleed, damage, getEffected(), getSkill());
		damage = getEffected().calcStat(Stats.BleedDef, damage, getEffected(), getSkill());
		getEffected().reduceCurrentHpByDOT(damage, getEffector(), getSkill());
		return true;
	}

	@Override
	public void onExit() {
		getEffected().stopAbnormalEffect(AbnormalEffect.BLEEDING);
	}
}
