package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * Effect will generate charges for Player targets Number of charges in "value", maximum number in "count" effect variables
 * @author DS
 */
@Effect("IncreaseCharges")
public class EffectIncreaseCharges extends L2Effect
{
	public EffectIncreaseCharges(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.INCREASE_CHARGES;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() == null)
			return false;
		
		if (!(getEffected() instanceof Player))
			return false;
		
		((Player) getEffected()).increaseCharges((int) calc(), getCount());
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false; // abort effect even if count > 1
	}
}