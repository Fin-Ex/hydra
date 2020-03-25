package net.sf.l2j.gameserver.skills.conditions;



import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.skills.Env;

/**
 * The Class ConditionPlayerHasClanHall.
 *
 * @author MrPoke
 */
public class ConditionPlayerHasClanHall extends Condition {

	private int[] clanHall;

	/**
	 * Instantiates a new condition player has clan hall.
	 *
	 * @param clanHall the clan hall
	 */
	public ConditionPlayerHasClanHall(int[] clanHall) {
		this.clanHall = clanHall;
	}

	/**
	 * @param env the env
	 * @return true, if successful
	 * @see
	 * net.sf.l2j.gameserver.skills.conditions.Condition#testImpl(net.sf.l2j.gameserver.skills.Env)
	 */
	@Override
	public boolean testImpl(Env env) {
		if (env.getPlayer() == null) {
			return false;
		}

		final Clan clan = env.getPlayer().getClan();
		if (clan == null) {
			return (clanHall.length == 1 && clanHall[0] == 0);
		}

		// All Clan Hall
		if (clanHall.length == 1 && clanHall[0] == -1) {
			return clan.hasHideout();
		}

		for(int i = 0; i < clanHall.length; i++) {
			if(clan.getHideoutId() == clanHall[i]) {
				return true;
			}
		}
		
		return false;
	}
}
