package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionTargetNpcId.
 */
public class ConditionTargetNpcId extends Condition {

	private int[] npcIDs;

	/**
	 * Instantiates a new condition target npc id.
	 *
	 * @param npcIds the npc ids
	 */
	public ConditionTargetNpcId(int[] npcIds) {
		npcIDs = npcIds;
	}

	@Override
	public boolean testImpl(Env env) {
		if (env.getTarget() instanceof Npc) {
			int npcId = ((Npc) env.getTarget()).getNpcId();
			for(int i = 0; i < npcIDs.length; i++) {
				if(npcIDs[i] == npcId) {
					return true;
				}
			}
			return false;
		}

		if (env.getTarget() instanceof Door) {
			int npcId = ((Door) env.getTarget()).getDoorId();
			for(int i = 0; i < npcIDs.length; i++) {
				if(npcIDs[i] == npcId) {
					return true;
				}
			}
			return false;
		}

		return false;
	}
}
