package sf.l2j.gameserver.skills.l2skills;

import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.StatsSet;

public class L2SkillDefault extends L2Skill {

	public L2SkillDefault(StatsSet set) {
		super(set);
	}

	@Override
	public void useSkill(Creature caster, WorldObject[] targets) {
		caster.sendPacket(ActionFailed.STATIC_PACKET);
		caster.sendMessage("Skill " + getId() + " [" + getSkillType() + "] isn't implemented.");
	}
}
