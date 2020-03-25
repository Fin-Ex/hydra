/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.talents;

import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbnormalEffect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.basefuncs.LambdaConst;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.templates.skills.EEffectBonusType;
import net.sf.l2j.gameserver.templates.skills.ESkillType;
import net.sf.finex.model.talents.ITalentHandler;

/**
 *
 * @author finfan
 */
@Slf4j
public class Aftershock implements ITalentHandler {

	@Override
	public EffectTemplate invoke(Object... args) {
		final StatsSet set = new StatsSet();
		set.set("attachCond");
		set.set("applayCond");
		set.set("lambda", new LambdaConst(0));
		set.set("count", 1);
		set.set("time", 4);
		set.set("abnormal", AbnormalEffect.HOLD_1);
		set.set("stackType", "paralyze");
		set.set("stackOrder", 4f);
		set.set("showIcon", true);
		set.set("effectPower", -1);
		set.set("effectType", ESkillType.PARALYZE);
		set.set("triggeredId", -1);
		set.set("triggeredLevel", -1);
		set.set("chanceType");
		set.set("bonus", EEffectBonusType.NONE);
		return new EffectTemplate("Paralyze", set);
	}

	public static final boolean validate(Creature creature, L2Skill skill) {
		if (!creature.isPlayer()) {
			return false;
		}

		return skill.getId() == 48 && creature.getPlayer().hasTalent(SkillTable.FrequentTalent.AFTERSHOCK);
	}
}
