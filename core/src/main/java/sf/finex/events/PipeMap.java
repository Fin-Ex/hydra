package sf.finex.events;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;

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
