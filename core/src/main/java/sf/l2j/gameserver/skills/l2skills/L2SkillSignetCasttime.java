package sf.l2j.gameserver.skills.l2skills;

import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.StatsSet;

public final class L2SkillSignetCasttime extends L2Skill {

	public int _effectNpcId;
	public int effectId;

	public L2SkillSignetCasttime(StatsSet set) {
		super(set);
		_effectNpcId = set.getInteger("effectNpcId", -1);
		effectId = set.getInteger("effectId", -1);
	}

	@Override
	public void useSkill(Creature caster, WorldObject[] targets) {
		if (caster.isAlikeDead()) {
			return;
		}

		getEffectsSelf(caster);
	}
}
