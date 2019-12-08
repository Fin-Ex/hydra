package net.sf.l2j.gameserver.scripting.scripts.ai.group;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.scripts.ai.L2AttackableAIScript;
import net.sf.l2j.gameserver.skills.L2Skill;

public class SearchingMaster extends L2AttackableAIScript
{
	private static final int[] MOBS =
	{
		20965,
		20966,
		20967,
		20968,
		20969,
		20970,
		20971,
		20972,
		20973
	};
	
	public SearchingMaster()
	{
		super("ai/group");
	}
	
	@Override
	protected void registerNpcs()
	{
		addAttackId(MOBS);
	}
	
	@Override
	public String onAttack(Npc npc, Player player, int damage, boolean isPet, L2Skill skill)
	{
		if (player == null)
			return null;
		
		attack(((Attackable) npc), player);
		return super.onAttack(npc, player, damage, isPet, skill);
	}
}