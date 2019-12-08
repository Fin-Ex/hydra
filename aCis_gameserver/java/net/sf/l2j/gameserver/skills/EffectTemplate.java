package net.sf.l2j.gameserver.skills;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.gameserver.model.ChanceCondition;
import net.sf.l2j.gameserver.skills.basefuncs.FuncTemplate;
import net.sf.l2j.gameserver.skills.basefuncs.Lambda;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.templates.skills.EEffectBonusType;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author mkizub
 */
@Slf4j
public final class EffectTemplate {

	private final Class<?> effectClass;
	private final Constructor<?> effectConstructor;
	public final Condition attachCond;
	public final Condition applayCond;
	public final Lambda lambda;
	public final int counter;
	public final int period; // in seconds
	public final AbnormalEffect abnormalEffect;
	public List<FuncTemplate> funcTemplates;
	public final String stackType;
	public final float stackOrder;
	public final boolean showIcon;
	public final double effectPower; // to handle chance
	public final ESkillType effectType; // to handle resistances etc...
	public final int triggeredId;
	public final int triggeredLevel;
	public final ChanceCondition chanceCondition;
	public final StatsSet stats;
	public final EEffectBonusType bonus;
	public EffectTemplate(String effectname, StatsSet set) {
		attachCond = set.getObject("attachCond", Condition.class);
		applayCond = set.getObject("applayCond", Condition.class);
		lambda = set.getObject("lambda", Lambda.class);
		counter = set.getInteger("count", 1);
		period = set.getInteger("time", 1);
		abnormalEffect = set.getEnum("abnormal", AbnormalEffect.class, AbnormalEffect.NULL);
		stackType = set.getString("stackType", "none");
		stackOrder = set.getFloat("stackOrder", 1f);
		showIcon = set.getBool("showIcon", true);
		effectPower = set.getDouble("effectPower", 100);
		effectType = set.getEnum("effectType", ESkillType.class, null);
		triggeredId = set.getInteger("triggeredId", -1);
		triggeredLevel = set.getInteger("triggeredLevel", -1);
		chanceCondition = set.getObject("chanceType", ChanceCondition.class);
		stats = set;
		
		effectClass = EffectClassHolder.getInstance().getClass(effectname);
		if(effectClass == null) {
			throw new RuntimeException("Backend effect of '" + effectname + "' not found!");
		}
		
		try {
			effectConstructor = effectClass.getConstructor(Env.class, EffectTemplate.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		
		bonus = set.getEnum("bonus", EEffectBonusType.class, EEffectBonusType.NONE);
	}

	public L2Effect getEffect(Env env) {
		if (attachCond != null && !attachCond.test(env)) {
			return null;
		}
		try {
			L2Effect effect = (L2Effect) effectConstructor.newInstance(env, this);
			return effect;
		} catch (IllegalAccessException | InstantiationException e) {
			log.error("", e);
			return null;
		} catch (InvocationTargetException e) {
			log.error("Error creating new instance of Class " + effectClass + " Exception was: " + e.getTargetException().getMessage(), e.getTargetException());
			return null;
		}
	}

	public void attach(FuncTemplate f) {
		if (funcTemplates == null) {
			funcTemplates = new ArrayList<>();
		}

		funcTemplates.add(f);
	}
}
