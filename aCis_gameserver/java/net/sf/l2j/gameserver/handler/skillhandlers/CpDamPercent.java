package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class CpDamPercent implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.CPDAMPERCENT
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (activeChar.isAlikeDead()) {
			return;
		}

		final boolean ss = activeChar.isChargedShot(ShotType.SOULSHOT);
		final boolean sps = activeChar.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOT);

		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (activeChar instanceof Player && target instanceof Player && ((Player) target).isFakeDeath()) {
				target.stopFakeDeath(true);
			} else if (target.isDead() || target.isInvul()) {
				continue;
			}

			final byte shld = Formulas.calcShldUse(activeChar, target, skill);
			final boolean parry = Formulas.calcParry(activeChar, target, skill);

			int damage = (int) (target.getCurrentCp() * (skill.getPower() / 100));

			// Manage cast break of the target (calculating rate, sending message...)
			Formulas.calcCastBreak(target, damage);

			skill.getEffects(activeChar, target, new Env(shld, ss, sps, bsps));
			activeChar.sendDamageMessage(target, damage, false, false, false, parry);
			target.setCurrentCp(target.getCurrentCp() - damage);

			// Custom message to see Wrath damage on target
			target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_GAVE_YOU_S2_DMG).addCharName(activeChar).addNumber(damage));
		}
		activeChar.setChargedShot(ShotType.SOULSHOT, skill.isStaticReuse());
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
