package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.data.SkillTable;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author Kerberos
 */
@Effect("Fusion")
public class EffectFusion extends L2Effect {

	public int _effect;
	public int _maxEffect;

	public EffectFusion(Env env, EffectTemplate template) {
		super(env, template);
		_effect = getSkill().getLevel();
		_maxEffect = SkillTable.getInstance().getMaxLevel(getSkill().getId());
	}

	@Override
	public boolean onActionTime() {
		return true;
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.FUSION;
	}

	public void increaseEffect() {
		if (_effect < _maxEffect) {
			_effect++;
			updateBuff();
		}
	}

	public void decreaseForce() {
		_effect--;
		if (_effect < 1) {
			exit();
		} else {
			updateBuff();
		}
	}

	private void updateBuff() {
		exit();
		SkillTable.getInstance().getInfo(getSkill().getId(), _effect).getEffects(getEffector(), getEffected());
	}
}
