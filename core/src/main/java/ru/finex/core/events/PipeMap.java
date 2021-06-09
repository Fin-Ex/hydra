package ru.finex.core.events;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

/**
 * PipeMap.java
 *
 * @author zcxv
 * @date 23.03.2018
 */
@RequiredArgsConstructor
class PipeMap<Input, Output> implements IEventPipe<Input, Output> {

	private final Function<Input, Output> function;

	@Override
	public Output process(Input object) {
		return function.apply(object);
	}

}
