package ru.finex.core.events;

/**
 * IEventPipe.java
 *
 * @author zcxv
 * @date 23.03.2018
 */
public interface IEventPipe<Input, Output> {

	Output process(Input object);

}
