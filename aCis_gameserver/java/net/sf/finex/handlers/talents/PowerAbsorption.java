/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.handlers.talents;

import java.util.List;
import net.sf.finex.model.talents.ITalentHandler;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbnormalEffect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.skills.basefuncs.FuncTemplate;
import net.sf.l2j.gameserver.skills.basefuncs.LambdaConst;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.templates.skills.EEffectBonusType;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 *
 * @author finfan
 */
public class PowerAbsorption implements ITalentHandler {

	@Override
	public EffectTemplate invoke(Object... args) {
		final List<WorldObject> targets = (List<WorldObject>) args[0];
		if(targets == null || targets.isEmpty()) {
			return null;
		}
		
		int summarized = 0;
		for(WorldObject obj : targets) {
			final int atk = obj.getCreature().getPAtk(null);
			summarized += atk - atk * 0.77;
		}
		final StatsSet set = new StatsSet();
		set.set("attachCond");
		set.set("applayCond");
		set.set("lambda", new LambdaConst(0));
		set.set("count", 1);
		set.set("time", 15);
		set.set("abnormal", AbnormalEffect.NULL);
		set.set("stackType", "howlUP");
		set.set("stackOrder", 1.f);
		set.set("showIcon", false);
		set.set("effectPower", -1);
		set.set("effectType", ESkillType.BUFF);
		set.set("triggeredId", -1);
		set.set("triggeredLevel", -1);
		set.set("chanceType");
		set.set("bonus", EEffectBonusType.NONE);
		set.set("self", true);
		final EffectTemplate et = new EffectTemplate("Buff", set);
		et.attach(new FuncTemplate(null, null, "Add", Stats.PAtk, 0x40, new LambdaConst(summarized)));
		return et;
	}

	public static final boolean validate(Creature creature, L2Skill skill) {
		if (!creature.isPlayer()) {
			return false;
		}

		return skill.getId() == 116 && creature.getPlayer().hasTalent(SkillTable.FrequentTalent.POWER_ABSOPTION);
	}
}
