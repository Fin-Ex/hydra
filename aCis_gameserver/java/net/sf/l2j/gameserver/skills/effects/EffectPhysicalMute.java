package net.sf.l2j.gameserver.skills.effects;

import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author -Nemesiss-
 */
@Effect("PhysicalMute")
public class EffectPhysicalMute extends L2Effect
{
	public EffectPhysicalMute(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.SILENCE;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().startMuted();
		if(getEffected().isPlayer()) {
			getEffected().getPlayer().sendSkillList();
		}
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopMuted(false);
		if(getEffected().isPlayer()) {
			getEffected().getPlayer().sendSkillList();
		}
	}
	
	@Override
	public int getEffectFlags()
	{
		return EEffectFlag.PHYSICAL_MUTED.getMask();
	}
}