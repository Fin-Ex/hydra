package sf.l2j.gameserver.skills.conditions;


import sf.l2j.gameserver.model.pledge.Clan;
import sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionPlayerHasCastle.
 *
 * @author MrPoke
 */
public final class ConditionPlayerHasCastle extends Condition {

	private int castle;

	/**
	 * Instantiates a new condition player has castle.
	 *
	 * @param castle the castle
	 */
	public ConditionPlayerHasCastle(int castle) {
		this.castle = castle;
	}

	/**
	 * @param env the env
	 * @return true, if successful
	 * @see
	 * Condition#testImpl(Env)
	 */
	@Override
	public boolean testImpl(Env env) {
		if (env.getPlayer() == null) {
			return false;
		}

		Clan clan = env.getPlayer().getClan();
		if (clan == null) {
			return castle == 0;
		}

		// Any castle
		if (castle == -1) {
			return clan.hasCastle();
		}

		return clan.getCastleId() == castle;
	}
}
