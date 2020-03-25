package net.sf.l2j.gameserver.skills.basefuncs;

import com.google.gson.annotations.JsonAdapter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.sf.finex.serializers.ConditionSerializer;
import net.sf.finex.serializers.LambdaSerializer;
import net.sf.finex.serializers.OrderSerializer;
import net.sf.finex.serializers.StatsSerializer;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mkizub
 */
//@JsonAdapter(FuncTemplateSerializer.class)
public final class FuncTemplate {

	protected static final Logger _log = LoggerFactory.getLogger(FuncTemplate.class.getName());

	@JsonAdapter(ConditionSerializer.class) public Condition attachCond;
	@JsonAdapter(ConditionSerializer.class) public Condition applayCond;
	public final String funcName;
	public final transient Constructor<?> constructor;
	@JsonAdapter(StatsSerializer.class) public final Stats stat;
	@JsonAdapter(OrderSerializer.class) public final int order;
	@JsonAdapter(LambdaSerializer.class) public final Lambda lambda;

	public FuncTemplate(Condition pAttachCond, Condition pApplayCond, String pFunc, Stats pStat, int pOrder, Lambda pLambda) {
		attachCond = pAttachCond;
		applayCond = pApplayCond;
		stat = pStat;
		order = pOrder;
		lambda = pLambda;
		funcName = pFunc;
		
		Class<?> func = null;
		try {
			func = Class.forName("net.sf.l2j.gameserver.skills.basefuncs.Func" + funcName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		try {
			constructor = func.getConstructor(new Class[]{
				Stats.class,
				Integer.TYPE,
				Object.class,
				Lambda.class
			});
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public Func getFunc(Env env, Object owner) {
		if (attachCond != null && !attachCond.test(env)) {
			return null;
		}

		try {
			Func f = (Func) constructor.newInstance(stat, order, owner, lambda);
			if (applayCond != null) {
				f.setCondition(applayCond);
			}
			return f;
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			_log.warn("", e);
			return null;
		}
	}
}
