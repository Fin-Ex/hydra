package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author Ahmed
 */
@Effect("ImmobileUntilAttacked")
public class EffectImmobileUntilAttacked extends L2Effect
{
	public EffectImmobileUntilAttacked(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.IMMOBILEUNTILATTACKED;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().startImmobileUntilAttacked();
		return true;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopImmobileUntilAttacked(this);
	}
	
	@Override
	public boolean onActionTime()
	{
		getEffected().stopImmobileUntilAttacked(this);
		// just stop this effect
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EEffectFlag.MEDITATING.getMask();
	}
}