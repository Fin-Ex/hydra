/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 *
 * @author FinFan
 */
@Effect("SongOfSilence")
public class EffectSongOfSilence extends L2Effect {

	public EffectSongOfSilence(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.SILENCE;
	}

	@Override
	public boolean onStart() {
		getEffected().startMuted();
		if (getEffected().isPlayer()) {
			getEffected().getPlayer().sendSkillList();
		}
		return true;
	}

	@Override
	public void onExit() {
		getEffected().stopMuted(false);
		if (getEffected().isPlayer()) {
			getEffected().getPlayer().sendSkillList();
		}
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.MAGIC_MUTED.getMask()
				| EEffectFlag.PHYSICAL_MUTED.getMask()
				| EEffectFlag.ABILITY_MUTED.getMask()
				| EEffectFlag.ULTIAMTE_MUTED.getMask();
	}
}
