package sf.l2j.gameserver.handler.skillhandlers;


import sf.l2j.commons.math.MathUtil;
import sf.l2j.gameserver.handler.IHandler;

import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.FlyToLocation;
import sf.l2j.gameserver.network.serverpackets.FlyToLocation.FlyType;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author Didldak Some parts taken from EffectWarp, which cannot be used for
 * this case.
 */
public class InstantJump implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.INSTANT_JUMP
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		Creature target = (Creature) targets[0];

		if (Formulas.calcPhysicalSkillEvasion(target, skill)) {
			if (activeChar instanceof Player) {
				((Player) activeChar).sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DODGES_ATTACK).addCharName(target));
			}

			return;
		}

		int x = 0, y = 0, z = 0;

		int px = target.getX();
		int py = target.getY();
		double ph = MathUtil.convertHeadingToDegree(target.getHeading());

		ph += 180;

		if (ph > 360) {
			ph -= 360;
		}

		ph = (Math.PI * ph) / 180;

		x = (int) (px + (25 * Math.cos(ph)));
		y = (int) (py + (25 * Math.sin(ph)));
		z = target.getZ();

		activeChar.getAI().setIntention(CtrlIntention.IDLE);
		activeChar.broadcastPacket(new FlyToLocation(activeChar, x, y, z, FlyType.DUMMY));
		activeChar.abortAttack();
		activeChar.abortCast();

		activeChar.setXYZ(x, y, z);
		activeChar.broadcastPacket(new ValidateLocation(activeChar));
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
