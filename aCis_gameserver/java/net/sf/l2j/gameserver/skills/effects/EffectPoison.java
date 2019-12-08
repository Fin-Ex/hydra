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
@Effect("Poison")
public class EffectPoison extends EffectDamOverTime {

	private final boolean targetIsUndead;

	public EffectPoison(Env env, EffectTemplate template) {
		super(env, template);
		targetIsUndead = getEffected().isUndead();
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.POISON;
	}

	@Override
	public boolean onStart() {
		getEffected().startAbnormalEffect(AbnormalEffect.POISON);
		return true;
	}

	@Override
	public boolean onActionTime() {
		damage = getEffector().calcStat(Stats.Poison, damage, getEffected(), getSkill());
		damage = getEffected().calcStat(Stats.PoisonDef, damage, getEffected(), getSkill());
		if(targetIsUndead) {
			getEffected().setCurrentHp(damage + getEffected().getCurrentHp());
		} else {
			getEffected().reduceCurrentHpByDOT(damage, getEffector(), getSkill());
		}
		return true;
	}

	@Override
	public void onExit() {
		getEffected().stopAbnormalEffect(AbnormalEffect.POISON);
	}
}
