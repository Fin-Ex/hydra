package ru.finex.gs.utils.variables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.finex.gs.utils.IGameVariable;
import ru.finex.gs.utils.RegexpPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author finfan
 */
@Slf4j
@Data
@AllArgsConstructor
public class Num extends Number implements IGameVariable {
	
	/**
	 * Never changeable value
	 */
	private Number base;
	/**
	 * Dynamic changeable value (this is for using)
	 */
	private Number actual;
	
	@Override
	public int intValue() {
		return actual.intValue();
	}
	
	@Override
	public long longValue() {
		return actual.longValue();
	}
	
	@Override
	public float floatValue() {
		return actual.floatValue();
	}
	
	@Override
	public double doubleValue() {
		return actual.doubleValue();
	}
	
	public boolean isMore(Number value) {
		return actual.longValue() > value.longValue();
	}
	
	public boolean isLess(Number value) {
		return actual.longValue() < value.longValue();
	}
	
	public boolean isEquals(Number value) {
		return actual.longValue() == value.longValue();
	}
	
	public Number mulAndGet(Number value) {
		return actual.doubleValue() * value.doubleValue();
	}
	
	public Number addAndGet(Number value) {
		return actual.doubleValue() + value.doubleValue();
	}
	
	public Number divAndGet(Number value) {
		return actual.doubleValue() / value.doubleValue();
	}
	
	public Number sunAndGet(Number value) {
		return actual.doubleValue() - value.doubleValue();
	}
	
	public Number sqrtAndGet() {
		return Math.sqrt(actual.doubleValue());
	}
	
	public Number powAndGet(Double pow) {
		return Math.pow(actual.doubleValue(), pow);
	}
	
	@Override
	public Num increment() {
		actual = actual.doubleValue() + 1;
		return this;
	}
	
	@Override
	public Num decrement() {
		actual = actual.doubleValue() - 1;
		return this;
	}
	
	@Override
	public void clean() {
		base = 0;
		actual = 0;
	}
	
	/**
	 * Calculate value and return it's result.<br> Example for calculator: {@code ($1 + $2) + ($3 * $4) + ... ($n+1 +
	 * $+2)}<br>
	 *
	 * @param formula given formula like ($1 + $2) + ($3 + $4) etc...
	 * @param values  given values gor calculating (values will be replaced $n each ordered)
	 * @return result of calculating
	 */
	public Number calcAndGet(String formula, Number... values) {
		List<String> strings = RegexpPattern.compileAndGet(formula, RegexpPattern.PatternType.BETWEEN_SIGN_BRACKET_CIRCLE);
		List<CalcExpression> expressions = new ArrayList<>();
		int index = 0;
		for (String s : strings) {
			String[] split = s.split(" ");
			expressions.add(new CalcExpression(values[index++], Double.valueOf(split[index++]),
				CalcExpressionType.valueOf(split[2])));
		}
		return expressions.stream().map(CalcExpression::calc)
			.collect(Collectors.summingDouble(Double::doubleValue));
	}
	
	@Data
	public class CalcExpression {
		private final Number left, right;
		private final CalcExpressionType operation;
		
		public Double calc() {
			return operation.calc(left, right).doubleValue();
		}
	}
	
	@AllArgsConstructor
	public enum CalcExpressionType {
		ADD("+") {
			@Override
			public Number calc(Number left, Number right) {
				return left.doubleValue() + right.doubleValue();
			}
		},
		SUB("-") {
			@Override
			public Number calc(Number left, Number right) {
				return left.doubleValue() - right.doubleValue();
			}
		},
		MUL("*") {
			@Override
			public Number calc(Number left, Number right) {
				return left.doubleValue() * right.doubleValue();
			}
		},
		DIV("/") {
			@Override
			public Number calc(Number left, Number right) {
				return left.doubleValue() / right.doubleValue();
			}
		};
		@Getter private final String sign;
		
		public abstract Number calc(Number left, Number right);
	}
}
