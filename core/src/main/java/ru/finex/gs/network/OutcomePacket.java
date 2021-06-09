package ru.finex.gs.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для исходящих пакетов от сервера.
 *
 * @author m0nster.mind
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OutcomePacket {

    /** Опкоды пакета. */
    Opcode[] value();

}
