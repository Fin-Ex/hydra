package net.sf.finex.events;

import org.slf4j.LoggerFactory;

/**
 * IEventPipe.java
 *
 * @author zcxv
 * @date 23.03.2018
 */
public interface IEventPipe<Input, Output> {

	Output process(Input object);

}
