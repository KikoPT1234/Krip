package pt.kiko.krip.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds some static fields used for lexing
 */
final public class Characters {

	/**
	 * Numbers
	 */
	static String digits = "0123456789";

	/**
	 * Letters
	 */
	static String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * Letters and digits (and '_')
	 */
	static String lettersDigits = digits + letters + "_";

	/**
	 * Array of keywords
	 */
	static String[] keywords = {
			"const",
			"let",
			"function",
			"if",
			"else if",
			"else",
			"for",
			"to",
			"while",
			"return",
			"break",
			"continue",
			"try",
			"catch",
			"finally"
	};

	/**
	 * Map of escaped characters
	 */
	static Map<String, String> escapedCharacters = new HashMap<>();

	static {
		Characters.escapedCharacters.put("n", "\\n");
		Characters.escapedCharacters.put("t", "\\t");
	}
}
