package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("Mute")
public class EffectMute extends L2Effect
{
	public EffectMute(Env env, EffectTemplate template)
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
		// Simply stop the effect
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
		return EEffectFlag.MAGIC_MUTED.getMask();
	}
}