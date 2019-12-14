package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.skills.AbnormalEffect;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("Grow")
public class EffectGrow extends L2Effect {

	public EffectGrow(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BUFF;
	}

	@Override
	public boolean onStart() {
		if (getEffected() instanceof Npc) {
			Npc npc = (Npc) getEffected();
			npc.setCollisionRadius(npc.getCollisionRadius() * 1.19);

			getEffected().startAbnormalEffect(AbnormalEffect.GROW);
			return true;
		}
		return false;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public void onExit() {
		if (getEffected() instanceof Npc) {
			Npc npc = (Npc) getEffected();
			npc.setCollisionRadius(npc.getTemplate().getCollisionRadius());

			getEffected().stopAbnormalEffect(AbnormalEffect.GROW);
		}
	}
}
