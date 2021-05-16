package sf.l2j.gameserver.skills.effects;

import java.util.ArrayList;
import java.util.List;

import sf.l2j.commons.random.Rnd;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Attackable;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.model.actor.instance.Chest;
import sf.l2j.gameserver.model.actor.instance.Door;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * This effect changes the target of the victim. It adds some random aggro
 * aswell to force the monster to keep attacking. As the added aggro is random,
 * the victim can often change of target.<br>
 * <br>
 * Any character can fill the aggroList of the victim. For a specialized use,
 * consider using EffectConfuseMob.
 *
 * @author littlecrow, Tryskell
 */
@Effect("Confusion")
public class EffectConfusion extends L2Effect {

	public EffectConfusion(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.CONFUSION;
	}

	/**
	 * Notify started
	 */
	@Override
	public boolean onStart() {
		getEffected().startConfused();
		onActionTime();
		return true;
	}

	/**
	 * Notify exited
	 */
	@Override
	public void onExit() {
		getEffected().stopConfused(this);
	}

	@Override
	public boolean onActionTime() {
		List<Creature> targetList = new ArrayList<>();

		// Getting the possible targets
		for (WorldObject obj : getEffected().getKnownType(WorldObject.class)) {
			// Attackable NPCs and playable characters (players, summons) are put in the list.
			if ((obj instanceof Attackable || obj instanceof Playable) && (obj != getEffected())) // Don't put doors nor chests on it.
			{
				if (!(obj instanceof Door || obj instanceof Chest)) {
					targetList.add((Creature) obj);
				}
			}
		}

		// if there is no target, exit function
		if (targetList.isEmpty()) {
			return true;
		}

		// Choosing randomly a new target
		WorldObject target = Rnd.get(targetList);

		// Attacking the target
		getEffected().setTarget(target);
		getEffected().getAI().setIntention(CtrlIntention.ATTACK, target);

		// Add aggro to that target aswell. The aggro power is random.
		int aggro = (5 + Rnd.get(5)) * getEffector().getLevel();
		((Attackable) getEffected()).addDamageHate((Creature) target, 0, aggro);

		return true;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.CONFUSED.getMask();
	}
}