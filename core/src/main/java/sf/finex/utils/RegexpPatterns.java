package sf.finex.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author finfan
 */
public class RegexpPatterns {
	
	@AllArgsConstructor
	public enum PatternType {
		BETWEEN_SIGN_BRACKET("(\\(.*\\))"),
		BETWEEN_SIGN_PERCENT("(\\%.*\\%)"),
		BETWEEN_SIGN_DOLLAR("(\\$.*\\$)"),
		;
		
		@Getter private final String patternExp;
	}
	
	public static String[] compileAndGet(String expression, PatternType... types) {
		StringBuilder fullPattern = new StringBuilder();
		for (PatternType t : types) {
			fullPattern = fullPattern.append(t.patternExp).append("|");
		}
		fullPattern.delete(fullPattern.length() - 1, fullPattern.length());
		
		Pattern pattern = Pattern.compile(fullPattern.toString());
		Matcher match = pattern.matcher(expression);
		String[] groups = new String[match.groupCount()];
		int index = 0;
		while (match.find()) {
			groups[index++] = match.group();
		}
		return groups;
	}
}
