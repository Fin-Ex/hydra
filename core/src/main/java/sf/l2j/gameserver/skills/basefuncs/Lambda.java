package sf.l2j.gameserver.skills.basefuncs;

import lombok.Setter;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.templates.skills.EEffectBonusType;

/**
 * @author mkizub
 */
public abstract class Lambda {

	@Setter protected EEffectBonusType bonus;

	public abstract double calc(Env env);
}
