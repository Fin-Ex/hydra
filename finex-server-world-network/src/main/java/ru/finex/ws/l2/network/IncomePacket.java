package ru.finex.ws.l2.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для входящих пакетов от клиента.
 *
 * @author m0nster.mind
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IncomePacket {

    /** Опкоды пакета. */
    Opcode[] value();

    /** Связанная команда с пакетом. */
    Class<? extends AbstractNetworkCommand> command() default NoOpCommand.class;

}