package sf.l2j.gameserver.network.serverpackets;

import java.util.HashMap;
import java.util.Map;
import sf.l2j.gameserver.model.group.Party;
import sf.l2j.gameserver.model.location.Location;

public class PartyMemberPosition extends L2GameServerPacket {

	private final Map<Integer, Location> locations = new HashMap<>();

	public PartyMemberPosition(Party party) {
		reuse(party);
	}

	public final void reuse(Party party) {
		locations.clear();
		party.getMembers().forEach(member -> locations.put(member.getObjectId(), new Location(member.getX(), member.getY(), member.getZ())));
	}

	@Override
	protected void writeImpl() {
		writeC(0xa7);
		writeD(locations.size());
		for (Map.Entry<Integer, Location> entry : locations.entrySet()) {
			final Location loc = entry.getValue();

			writeD(entry.getKey());
			writeD(loc.getX());
			writeD(loc.getY());
			writeD(loc.getZ());
		}
	}
}
