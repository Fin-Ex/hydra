package sf.l2j.gameserver.skills;

import org.slf4j.LoggerFactory;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
/**
 * Effect
 *
 * @author zcxv
 * @date 19.05.2019
 */
public @interface Effect {

	/**
	 * Effect name
	 */
	String value();

}
