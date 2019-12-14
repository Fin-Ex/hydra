package net.sf.l2j.gameserver.model.olympiad;

import org.slf4j.LoggerFactory;

import java.util.List;

import net.sf.l2j.Config;

/**
 * @author DS
 */
public class OlympiadGameNonClassed extends OlympiadGameNormal {

	private OlympiadGameNonClassed(int id, Participant[] opponents) {
		super(id, opponents);
	}

	@Override
	public final CompetitionType getType() {
		return CompetitionType.NON_CLASSED;
	}

	@Override
	protected final int getDivider() {
		return Config.ALT_OLY_DIVIDER_NON_CLASSED;
	}

	@Override
	protected final int[][] getReward() {
		return Config.ALT_OLY_NONCLASSED_REWARD;
	}

	protected static final OlympiadGameNonClassed createGame(int id, List<Integer> list) {
		final Participant[] opponents = OlympiadGameNormal.createListOfParticipants(list);
		if (opponents == null) {
			return null;
		}

		return new OlympiadGameNonClassed(id, opponents);
	}
}
