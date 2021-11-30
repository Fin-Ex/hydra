package ru.finex.ws.l2.network;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, которую обходимо устанавливать на {@link ru.finex.ws.l2.network.model.NetworkDto}
 *  чтобы при обработке данных пакета была вызвана определенная команда.
 * @author m0nster.mind
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {

    Class<? extends AbstractNetworkCommand> value() default NoOpCommand.class;

}
