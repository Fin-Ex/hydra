package net.sf.l2j.gameserver.handler.skillhandlers;


import net.sf.finex.model.creature.attack.DamageInfo;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author _tomciaaa_
 */
public class StrSiegeAssault implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.STRSIEGEASSAULT
	};

	@Override
	public void invoke(Object... args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (!(activeChar instanceof Player)) {
			return;
		}

		final Player player = (Player) activeChar;

		if (!player.checkIfOkToUseStriderSiegeAssault(skill)) {
			return;
		}

		int damage = 0;

		final boolean ss = activeChar.isChargedShot(ShotType.SOULSHOT);

		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (target.isAlikeDead()) {
				continue;
			}

			final DamageInfo info = new DamageInfo();

			info.shieldResult = Formulas.calcShldUse(activeChar, target, null);
			info.isCrit = Formulas.calcCrit(activeChar.getCriticalHit(target, skill));

			if (!info.isCrit && (skill.getCondition() & L2Skill.COND_CRIT) != 0) {
				damage = 0;
			} else {
				damage = (int) Formulas.calcPhysDam(activeChar, target, skill, info, ss);
			}

			if (damage > 0) {
				activeChar.sendDamageMessage(target, damage, false, false, false, false);
				target.reduceCurrentHp(damage, activeChar, skill);
			} else {
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ATTACK_FAILED));
			}
		}
		activeChar.setChargedShot(ShotType.SOULSHOT, skill.isStaticReuse());
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
