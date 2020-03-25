package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.effects.EffectFusion;

/**
 * The Class ConditionForceBuff.
 *
 * @author kombat, Forsaiken
 */
public class ConditionForceBuff extends Condition {

	private static transient final short BATTLE_FORCE = 5104;
	private static transient final short SPELL_FORCE = 5105;

	private byte[] forces;

	/**
	 * Instantiates a new condition force buff.
	 *
	 * @param forces the forces
	 */
	public ConditionForceBuff(byte[] forces) {
		this.forces = forces;
	}

	/**
	 * Test impl.
	 *
	 * @param env the env
	 * @return true, if successful
	 * @see
	 * net.sf.l2j.gameserver.skills.conditions.Condition#testImpl(net.sf.l2j.gameserver.skills.Env)
	 */
	@Override
	public boolean testImpl(Env env) {
		if (forces[0] > 0) {
			L2Effect force = env.getCharacter().getFirstEffect(BATTLE_FORCE);
			if (force == null || ((EffectFusion) force)._effect < forces[0]) {
				return false;
			}
		}

		if (forces[1] > 0) {
			L2Effect force = env.getCharacter().getFirstEffect(SPELL_FORCE);
			if (force == null || ((EffectFusion) force)._effect < forces[1]) {
				return false;
			}
		}
		return true;
	}
}
