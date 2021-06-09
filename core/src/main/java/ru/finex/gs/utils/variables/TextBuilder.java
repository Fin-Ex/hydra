package ru.finex.gs.utils.variables;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.finex.gs.utils.IText;
import ru.finex.gs.utils.RegexpPattern;

import java.util.List;

/**
 * @author finfan
 */
@Slf4j
@Data
public class TextBuilder implements IText {
	
	private final StringBuilder text;
	
	public TextBuilder() {
		this.text = new StringBuilder();
	}
	
	public TextBuilder clear() {
		text.setLength(0);
		return this;
	}
	
	public TextBuilder append(String text) {
		this.text.append(text);
		return this;
	}
	
	public TextBuilder append(int fromIndex, boolean withSpaceAtEnd, String text) {
		this.text.insert(fromIndex, text);
		if (withSpaceAtEnd) {
			append(" ");
		}
		return this;
	}
	
	public TextBuilder replace(Object... values) {
		List<String> regexp = RegexpPattern.compileAndGet(text.toString(), RegexpPattern.PatternType.BETWEEN_SIGN_DOLLAR,
			RegexpPattern.PatternType.BETWEEN_SIGN_PERCENT);
		String replace = text.toString();
		for (int i = 0; i < regexp.size(); i++) {
			replace = replace.replace(regexp.get(i), values[i].toString());
		}
		clear();
		append(replace);
		return this;
	}
}
