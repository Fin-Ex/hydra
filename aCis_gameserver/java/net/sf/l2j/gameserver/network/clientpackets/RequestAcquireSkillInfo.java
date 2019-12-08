package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.SkillTreeTable;
import net.sf.l2j.gameserver.data.xml.SpellbookData;
import net.sf.l2j.gameserver.model.L2PledgeSkillLearn;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.AcquireSkillInfo;
import net.sf.l2j.gameserver.skills.L2Skill;

public class RequestAcquireSkillInfo extends L2GameClientPacket
{
	private int _skillId;
	private int _skillLevel;
	private int _skillType;
	
	@Override
	protected void readImpl()
	{
		_skillId = readD();
		_skillLevel = readD();
		_skillType = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (_skillId <= 0 || _skillLevel <= 0)
			return;
		
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		final Npc trainer = activeChar.getCurrentFolkNPC();
		if (trainer == null)
			return;
		
		if (!activeChar.isInsideRadius(trainer, Npc.INTERACTION_DISTANCE, false, false) && !activeChar.isGM())
			return;
		
		final L2Skill skill = SkillTable.getInstance().getInfo(_skillId, _skillLevel);
		if (skill == null)
			return;
		
		switch (_skillType)
		{
			// General skills
			case 0:
				int skillLvl = activeChar.getSkillLevel(_skillId);
				if (skillLvl >= _skillLevel)
					return;
				
				if (Math.max(skillLvl, 0) + 1 != _skillLevel)
					return;
				
				if (!trainer.getTemplate().canTeach(activeChar.getSkillLearningClassId()))
					return;
				
				for (L2SkillLearn sl : SkillTreeTable.getInstance().getAvailableSkills(activeChar, activeChar.getSkillLearningClassId()))
				{
					if (sl.getId() == _skillId && sl.getLevel() == _skillLevel)
					{
						AcquireSkillInfo asi = new AcquireSkillInfo(_skillId, _skillLevel, sl.getSpCost(), 0);
						int spellbookItemId = SpellbookData.getInstance().getBookForSkill(_skillId, _skillLevel);
						if (spellbookItemId != 0)
							asi.addRequirement(99, spellbookItemId, 1, 50);
						sendPacket(asi);
						break;
					}
				}
				break;
			// Common skills
			case 1:
				skillLvl = activeChar.getSkillLevel(_skillId);
				if (skillLvl >= _skillLevel)
					return;
				
				if (Math.max(skillLvl, 0) + 1 != _skillLevel)
					return;
				
				for (L2SkillLearn sl : SkillTreeTable.getInstance().getAvailableFishingDwarvenCraftSkills(activeChar))
				{
					if (sl.getId() == _skillId && sl.getLevel() == _skillLevel)
					{
						AcquireSkillInfo asi = new AcquireSkillInfo(_skillId, _skillLevel, sl.getSpCost(), 1);
						asi.addRequirement(4, sl.getIdCost(), sl.getCostCount(), 0);
						sendPacket(asi);
						break;
					}
				}
				break;
			// Pledge skills.
			case 2:
				if (!activeChar.isClanLeader())
					return;
				
				for (L2PledgeSkillLearn psl : SkillTreeTable.getInstance().getAvailablePledgeSkills(activeChar))
				{
					if (psl.getId() == _skillId && psl.getLevel() == _skillLevel)
					{
						AcquireSkillInfo asi = new AcquireSkillInfo(skill.getId(), skill.getLevel(), psl.getRepCost(), 2);
						if (Config.LIFE_CRYSTAL_NEEDED && psl.getItemId() != 0)
							asi.addRequirement(1, psl.getItemId(), 1, 0);
						sendPacket(asi);
						break;
					}
				}
				break;
		}
	}
}