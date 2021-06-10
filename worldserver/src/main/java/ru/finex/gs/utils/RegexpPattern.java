package ru.finex.gs.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author finfan
 */
public class RegexpPattern {
	
	@AllArgsConstructor
	public enum PatternType {
		BETWEEN_SIGN_BRACKET_CIRCLE("[\\(](.+?)[\\)]"),
		BETWEEN_SIGN_BRACKET_SQUARE("[\\[](.+?)[\\]]"),
		BETWEEN_SIGN_PERCENT("[\\[%](.+?)[\\%]"),
		BETWEEN_SIGN_DOLLAR("[\\[$](.+?)[\\$]"),
		;
		
		@Getter private final String patternExp;
	}
	
	public static List<String> compileAndGet(String expression, PatternType... types) {
		StringBuilder fullPattern = new StringBuilder();
		for (PatternType t : types) {
			fullPattern = fullPattern.append(t.patternExp).append("|");
		}
		fullPattern.delete(fullPattern.length() - 1, fullPattern.length());
		Matcher match = Pattern.compile(fullPattern.toString()).matcher(expression);
		List<String> matches = new ArrayList<>();
		while (match.find()) {
			matches.add(match.group());
		}
		return matches;
	}
}
