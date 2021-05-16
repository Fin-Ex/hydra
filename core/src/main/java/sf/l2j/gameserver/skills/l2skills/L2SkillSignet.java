package sf.l2j.gameserver.skills.l2skills;

import sf.l2j.gameserver.data.NpcTable;
import sf.l2j.gameserver.idfactory.IdFactory;
import sf.finex.enums.ESkillTargetType;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.instance.EffectPoint;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.model.location.Location;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.StatsSet;

public final class L2SkillSignet extends L2Skill {

	private final int _effectNpcId;
	public int effectId;

	public L2SkillSignet(StatsSet set) {
		super(set);
		_effectNpcId = set.getInteger("effectNpcId", -1);
		effectId = set.getInteger("effectId", -1);
	}

	@Override
	public void useSkill(Creature caster, WorldObject[] targets) {
		if (caster.isAlikeDead()) {
			return;
		}

		NpcTemplate template = NpcTable.getInstance().getTemplate(_effectNpcId);
		EffectPoint effectPoint = new EffectPoint(IdFactory.getInstance().getNextId(), template, caster);
		effectPoint.setCurrentHp(effectPoint.getMaxHp());
		effectPoint.setCurrentMp(effectPoint.getMaxMp());

		int x = caster.getX();
		int y = caster.getY();
		int z = caster.getZ();

		if (caster instanceof Player && getTargetType() == ESkillTargetType.TARGET_GROUND) {
			Location wordPosition = ((Player) caster).getCurrentSkillWorldPosition();

			if (wordPosition != null) {
				x = wordPosition.getX();
				y = wordPosition.getY();
				z = wordPosition.getZ();
			}
		}
		getEffects(caster, effectPoint);

		effectPoint.setIsInvul(true);
		effectPoint.spawnMe(x, y, z);
	}
}
