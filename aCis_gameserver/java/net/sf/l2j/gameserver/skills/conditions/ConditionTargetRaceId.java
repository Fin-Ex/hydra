package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author nBd
 */
public class ConditionTargetRaceId extends Condition {

	private int[] raceIDs;

	public ConditionTargetRaceId(int[] raceId) {
		raceIDs = raceId;
	}

	@Override
	public boolean testImpl(Env env) {
		if (!(env.getTarget() instanceof Npc)) {
			return false;
		}

		final int raceOrdinal = ((Npc) env.getTarget()).getTemplate().getRace().ordinal();
		for (int i = 0; i < raceIDs.length; i++) {
			if(raceIDs[i] == raceOrdinal) {
				return true;
			}
		}
		
		return false;
	}
}
