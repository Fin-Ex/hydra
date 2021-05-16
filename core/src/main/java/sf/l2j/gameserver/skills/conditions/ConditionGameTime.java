package sf.l2j.gameserver.skills.conditions;


import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

/**
 * @author mkizub
 */
public class ConditionGameTime extends Condition {

	private boolean night;

	public ConditionGameTime(boolean night) {
		this.night = night;
	}

	@Override
	public boolean testImpl(Env env) {
		return GameTimeTaskManager.getInstance().isNight() == night;
	}
}
