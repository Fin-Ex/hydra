package net.sf.l2j.gameserver.handler.skillhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class ManaHeal implements ISkillHandler
{
	private static final ESkillType[] SKILL_IDS =
	{
		ESkillType.MANAHEAL,
		ESkillType.MANARECHARGE
	};
	
	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets)
	{
		for (WorldObject obj : targets)
		{
			if (!(obj instanceof Creature))
				continue;
			
			final Creature target = ((Creature) obj);
			if (target.isInvul())
				continue;
			
			double mp = skill.getPower();
			
			if (skill.getSkillType() == ESkillType.MANAHEAL_PERCENT)
				mp = target.getMaxMp() * mp / 100.0;
			else
				mp = (skill.getSkillType() == ESkillType.MANARECHARGE) ? target.calcStat(Stats.GainMP, mp, null, null) : mp;
			
			// It's not to be the IL retail way, but it make the message more logical
			if ((target.getCurrentMp() + mp) >= target.getMaxMp())
				mp = target.getMaxMp() - target.getCurrentMp();
			
			target.setCurrentMp(mp + target.getCurrentMp());
			StatusUpdate sump = new StatusUpdate(target);
			sump.addAttribute(StatusUpdate.CUR_MP, (int) target.getCurrentMp());
			target.sendPacket(sump);
			
			if (activeChar instanceof Player && activeChar != target)
				target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_MP_RESTORED_BY_S1).addCharName(activeChar).addNumber((int) mp));
			else
				target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_MP_RESTORED).addNumber((int) mp));
		}
		
		if (skill.hasSelfEffects())
		{
			final L2Effect effect = activeChar.getFirstEffect(skill.getId());
			if (effect != null && effect.isSelfEffect())
				effect.exit();
			
			skill.getEffectsSelf(activeChar);
		}
		
		if (!skill.isPotion())
			activeChar.setChargedShot(activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOT) ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}
	
	@Override
	public ESkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}