package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.model.zone.ZoneId;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.skills.L2Skill;

public class SiegeSummon extends Servitor {

	public static final int SIEGE_GOLEM_ID = 14737;
	public static final int HOG_CANNON_ID = 14768;
	public static final int SWOOP_CANNON_ID = 14839;

	public SiegeSummon(int objectId, NpcTemplate template, Player owner, L2Skill skill) {
		super(objectId, template, owner, skill);
	}

	@Override
	public void onSpawn() {
		super.onSpawn();
		if (!isInsideZone(ZoneId.SIEGE)) {
			unSummon(getPlayer());
			getPlayer().sendPacket(SystemMessageId.YOUR_SERVITOR_HAS_VANISHED);
		}
	}
}
