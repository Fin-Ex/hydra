/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.skills.AbnormalEffect;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

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
