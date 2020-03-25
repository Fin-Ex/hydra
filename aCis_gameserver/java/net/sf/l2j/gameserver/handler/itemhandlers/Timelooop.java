/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.instance.Trap;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.skills.AbnormalEffect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.basefuncs.LambdaConst;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.templates.skills.EEffectBonusType;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 *
 * @author finfan
 */
public class Timelooop implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		final Trap trap = Trap.spawn(50010, playable.getX(), playable.getY(), playable.getZ(), 0);
		trap.activate(15, 1);
		trap.setImplcode(() -> {
			//try found target which was victim
			for(Playable target : trap.getKnownTypeInRadius(Playable.class, 50)) {
				if (target != null) {
					final EffectTemplate template = template();
					final Env env = new Env();
					env.setCharacter(playable);
					env.setTarget(target);
					final L2Effect e = template.getEffect(env);
					if (e != null) {
						e.scheduleEffect();
					}
					trap.deactivate();
					break;
				}
			}		
		});
	}
	
	private EffectTemplate template() {
		final StatsSet set = new StatsSet();
		set.set("attachCond");
		set.set("applayCond");
		set.set("lambda", new LambdaConst(0));
		set.set("count", 1);
		set.set("time", 30);
		set.set("abnormal", AbnormalEffect.NULL);
		set.set("stackType", "timeloop");
		set.set("stackOrder", 1f);
		set.set("showIcon", true);
		set.set("effectPower", -1);
		set.set("effectType", ESkillType.DEBUFF);
		set.set("triggeredId", -1);
		set.set("triggeredLevel", -1);
		set.set("chanceType");
		set.set("bonus", EEffectBonusType.NONE);
		return new EffectTemplate("Timelooop", set);
	}
}
