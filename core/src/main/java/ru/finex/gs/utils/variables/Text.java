package ru.finex.gs.utils.variables;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.finex.gs.utils.IText;
import ru.finex.gs.utils.RegexpPattern;

import java.util.Arrays;
import java.util.List;

/**
 * @author finfan
 */
@Slf4j
@Data
public class Text implements IText {
	
	private String text;
	
	public Text(String text) {
		this.text = text;
	}
	
	public Text() {
		this.text = "";
	}
	
	public Text append(String text) {
		this.text = this.text.concat(text);
		return this;
	}
	
	public Text replace(Object... values) {
		List<String> regexp = RegexpPattern.compileAndGet(text, RegexpPattern.PatternType.BETWEEN_SIGN_DOLLAR,
			RegexpPattern.PatternType.BETWEEN_SIGN_PERCENT);
		for (int i = 0; i < regexp.size(); i++) {
			text = text.replace(regexp.get(i), values[i].toString());
		}
		return this;
	}
	
	public Num toNum() {
		Number result = Double.valueOf(text);
		return new Num(result, result);
	}
	
	public Number toNumber() {
		return Double.valueOf(text);
	}

	public static Num toNum(String value) {
		Number result = Double.valueOf(value);
		return new Num(result, result);
	}
	
	public static Number toNumber(String value) {
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
