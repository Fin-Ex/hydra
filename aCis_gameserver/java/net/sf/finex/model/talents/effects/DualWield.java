package net.sf.finex.model.talents.effects;

import org.slf4j.LoggerFactory;

import net.sf.finex.events.AbstractEventSubscription;
import net.sf.l2j.gameserver.model.actor.events.OnDualHit;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * Урон от дуалов больше не разделяется и каждый клинок наносит
 *  ровно столько урона, сколько наносили оба вместе.
 *
 * @author zcxv
 * @date 19.05.2019
 */
@Effect("DualWield")
public class DualWield extends L2Effect {

	private AbstractEventSubscription<OnDualHit> hitSubscribtion;
	
	public DualWield(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BUFF;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
	
	@Override
	public boolean onStart() {
		hitSubscribtion = getEffector().getEventBus().subscribe()
				.cast(OnDualHit.class)
				.forEach(this::onHit);
		
		return super.onStart();
	}
	
	@Override
	public void onExit() {
		getEffector().getEventBus().unsubscribe(hitSubscribtion);
		super.onExit();
	}
	
	private void onHit(OnDualHit e) {
		e.getDamageInfo().damage *= 2;
	}
	
}
