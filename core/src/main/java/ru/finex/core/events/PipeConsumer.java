package ru.finex.core.events;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

/**
 * EventAction.java
 *
 * @author zcxv
 * @date 23.03.2018
 */
@RequiredArgsConstructor
class PipeConsumer<Input> implements IEventPipe<Input, Input> {

	private final Consumer<Input> consumer;

	@Override
	public Input process(Input object) {
		consumer.accept(object);
		return object;
	}

}
