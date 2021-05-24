package sf.finex.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author finfan
 */
@Slf4j
@Data
public class Text implements IGameVariable {
	private final StringBuilder stringBuilder = new StringBuilder();
	private String string;
	
	public Text(String string) {
		this.string = string;
	}
	
	public Text append(String text) {
		stringBuilder.append(text);
		return this;
	}
	
	public Text append(StringBuilder stringBuilder) {
		stringBuilder.append(stringBuilder.toString());
		return this;
	}
	
	public Text concat(String text) {
		this.string.concat(text);
		return this;
	}
	
	public Text replace(Object... values) {
		String[] regexp = RegexpPatterns.compileAndGet(string, RegexpPatterns.PatternType.BETWEEN_SIGN_DOLLAR,
			RegexpPatterns.PatternType.BETWEEN_SIGN_PERCENT);
		for (int i = 0; i < regexp.length; i++) {
			string = string.replace(regexp[i], values[i].toString());
		}
		return this;
	}
	
	public Num toNum(String value) {
		Number result = Double.valueOf(value);
		return new Num(result, result);
	}
	
	public Number toNumber(String value) {
		return Double.valueOf(value);
	}
	
	@Deprecated
	public Number[] toNumberArray(String expression, String delim) {
		String[] split = expression.split(delim);
		Number[] array = new Number[split.length];
		try {
			for (int i = 0; i < split.length; i++) {
				array[i] = Double.valueOf(split[i]);
			}
			return array;
		} catch (Exception e) {
			log.warn("Wrong parse from {} with delim {}. Array filled by zero for avoid null state!", expression, delim);
		}
		Arrays.fill(array, 0, array.length, 0);
		return array;
	}
	
	@Deprecated
	public Num[] toNumArray(String expression, String delim) {
		Number[] numbers = toNumberArray(expression, delim);
		Num[] array = new Num[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
			array[i] = new Num(numbers[i], numbers[i]);
		}
		return array;
	}
	
	public static <T> Text from(T t) {
		return new Text(t.toString());
	}
}
