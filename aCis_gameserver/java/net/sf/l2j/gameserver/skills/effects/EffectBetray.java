package net.sf.l2j.gameserver.skills.effects;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author decad
 */
@Effect("Betray")
public class EffectBetray extends L2Effect {

	public EffectBetray(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BETRAY;
	}

	/**
	 * Notify started
	 */
	@Override
	public boolean onStart() {
		if (getEffector() instanceof Player && getEffected() instanceof Summon) {
			Player targetOwner = getEffected().getPlayer();
			getEffected().getAI().setIntention(CtrlIntention.ATTACK, targetOwner);
			return true;
		}
		return false;
	}

	/**
	 * Notify exited
	 */
	@Override
	public void onExit() {
		getEffected().getAI().setIntention(CtrlIntention.IDLE);
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.BETRAYED.getMask();
	}
}
