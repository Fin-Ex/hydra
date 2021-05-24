package sf.finex.utils;

/**
 * @author finfan
 */
public interface IGameVariable {
	default <T> T increment() {
		throw new UnsupportedOperationException("Method increment in " + getClass() + " is not supported!");
	}
	
	default <T> T decrement() {
		throw new UnsupportedOperationException("Method decrement in " + getClass() + " is not supported!");
	}
	
	default void clean() {
		throw new UnsupportedOperationException("Method clean in " + getClass() + " is not supported!");
	}
}
