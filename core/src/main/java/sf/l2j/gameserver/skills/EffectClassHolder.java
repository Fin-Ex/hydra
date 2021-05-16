package sf.l2j.gameserver.skills;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import sf.l2j.gameserver.GameServer;

/**
 * EffectClassHolder
 *
 * @author zcxv
 * @date 19.05.2019
 */
public class EffectClassHolder {

	@Getter
	private final static EffectClassHolder instance = new EffectClassHolder();

	private Map<String, Class<? extends L2Effect>> classes;

	public EffectClassHolder() {
		load();
	}

	private void load() {
		classes = GameServer.getReflections().getTypesAnnotatedWith(Effect.class).stream()
				.filter(e -> L2Effect.class.isAssignableFrom(e))
				.filter(e -> !Modifier.isAbstract(e.getModifiers()))
				.collect(Collectors.toMap(e -> e.getAnnotation(Effect.class).value(), e -> (Class<? extends L2Effect>) e));
	}

	public Class<? extends L2Effect> getClass(String name) {
		return classes.get(name);
	}

}
