package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("BlockBuff")
public class EffectBlockBuff extends L2Effect
{
	public EffectBlockBuff(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.BLOCK_BUFF;
	}
	
	@Override
	public boolean onStart()
	{
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}