package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public class ConditionPlayerState extends Condition {

	public enum PlayerState {
		RESTING,
		MOVING,
		RUNNING,
		RIDING,
		FLYING,
		BEHIND,
		FRONT,
		OLYMPIAD
	}

	private PlayerState check;
	private boolean required;

	public ConditionPlayerState(PlayerState check, boolean required) {
		this.check = check;
		this.required = required;
	}

	@Override
	public boolean testImpl(Env env) {
		final Creature character = env.getCharacter();
		final Player player = env.getPlayer();

		switch (check) {
			case RESTING:
				return (player == null) ? !required : player.isSitting() == required;

			case MOVING:
				return character.isMoving() == required;

			case RUNNING:
				return character.isMoving() == required && character.isRunning() == required;

			case RIDING:
				return character.isRiding() == required;

			case FLYING:
				return character.isFlying() == required;

			case BEHIND:
				return character.isBehindTarget() == required;

			case FRONT:
				return character.isInFrontOfTarget() == required;

			case OLYMPIAD:
				return (player == null) ? !required : player.isInOlympiadMode() == required;
		}
		return !required;
	}
}
