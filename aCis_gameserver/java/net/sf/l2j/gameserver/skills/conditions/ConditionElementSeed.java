package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.effects.EffectSeed;

/**
 * @author Advi
 */
public class ConditionElementSeed extends Condition {

	private static transient final int[] SEED_SKILLS = {
		1285,
		1286,
		1287
	};
	
	private int[] requiredSeeds;

	public ConditionElementSeed(int[] seeds) {
		requiredSeeds = seeds;
	}

	@Override
	public boolean testImpl(Env env) {
		int[] Seeds = new int[3];
		for (int i = 0; i < Seeds.length; i++) {
			Seeds[i] = (env.getCharacter().getFirstEffect(SEED_SKILLS[i]) instanceof EffectSeed ? ((EffectSeed) env.getCharacter().getFirstEffect(SEED_SKILLS[i])).getPower() : 0);
			if (Seeds[i] >= requiredSeeds[i]) {
				Seeds[i] -= requiredSeeds[i];
			} else {
				return false;
			}
		}

		if (requiredSeeds[3] > 0) {
			int count = 0;
			for (int i = 0; i < Seeds.length && count < requiredSeeds[3]; i++) {
				if (Seeds[i] > 0) {
					Seeds[i]--;
					count++;
				}
			}
			if (count < requiredSeeds[3]) {
				return false;
			}
		}

		if (requiredSeeds[4] > 0) {
			int count = 0;
			for (int i = 0; i < Seeds.length && count < requiredSeeds[4]; i++) {
				count += Seeds[i];
			}
			if (count < requiredSeeds[4]) {
				return false;
			}
		}

		return true;
	}
}
