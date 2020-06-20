package pt.kiko.krip.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.kiko.krip.lang.errors.LexError;
import pt.kiko.krip.lang.results.LexResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Lexer is responsible for making tokens from the given code string
 */

public class Lexer {

	String code;
	Position position;
	char currentChar;
	private final ArrayList<Token> tokens = new ArrayList<>();
	char nextChar;
	private LexError error;

	/**
	 * @param code     The code to make tokens from
	 * @param fileName The file name for errors
	 * @see Token
	 */

	public Lexer(@NotNull String code, String fileName) {
		this.code = code.replaceAll("\\r", "");

		position = new Position(-1, 0, -1, fileName, code);
		advance();
	}

	/**
	 * Advances to the next character
	 */

	public void advance() {
		position.advance(currentChar);
		if (position.index < code.length()) {
			currentChar = code.charAt(position.index);
			if (position.index + 1 < code.length()) {
				nextChar = code.charAt(position.index + 1);
			}
		} else currentChar = Character.MIN_VALUE;
	}

	/**
	 * Makes the tokens
	 *
	 * @return A LexResult containing the list of tokens
	 * @see LexResult
	 * @see Token
	 */

	public LexResult makeTokens() {

		while (position.index < code.length()) {
			Token token = makeToken();
			if (error != null) return new LexResult().failure(error);

			if (token != null) tokens.add(token);
		}
		tokens.add(new Token(TokenTypes.EOF, position));
		return new LexResult().success(tokens);

	}

	private @Nullable Token makeToken() {
		Token returnToken = null;
		if (String.valueOf(currentChar).matches("[;\\n]")) {
			if (currentChar == ';') {
				Position startPosition = position.copy();
				advance();
				returnToken = new Token(TokenTypes.NEWLINE, startPosition);
				if (String.valueOf(currentChar).equals("\n")) {
					advance();
				}
				return returnToken;
			}
			returnToken = new Token(TokenTypes.NEWLINE, position);
			advance();
		} else if (String.valueOf(currentChar).matches("\\s")) advance();
		else if (Characters.letters.contains(String.valueOf(currentChar))) {
			returnToken = makeIdentifier();
		} else if (Characters.digits.contains(String.valueOf(currentChar))) {
			returnToken = makeNumber();
		} else if (currentChar == '"' || currentChar == '\'' || currentChar == '`') {
			returnToken = makeString();
		} else if (currentChar == '+') {
			returnToken = new Token(TokenTypes.PLUS, position);
			advance();
		} else if (currentChar == '-') {
			returnToken = new Token(TokenTypes.MINUS, position);
			advance();
		} else if (currentChar == '*') {
			returnToken = new Token(TokenTypes.MUL, position);
			advance();
		} else if (currentChar == '^') {
			returnToken = new Token(TokenTypes.POW, position);
			advance();
		} else if (currentChar == '%') {
			returnToken = new Token(TokenTypes.MOD, position);
			advance();
		} else if (currentChar == '/') {
			Token token = makeDivOrComment();
			if (token == null) return null;
			returnToken = token;
		} else if (currentChar == '(') {
			returnToken = new Token(TokenTypes.LPAREN, position);
			advance();
		} else if (currentChar == ')') {
			returnToken = new Token(TokenTypes.RPAREN, position);
			advance();
		} else if (currentChar == '[') {
			returnToken = new Token(TokenTypes.LSQUARE, position);
			advance();
		} else if (currentChar == ']') {
			returnToken = new Token(TokenTypes.RSQUARE, position);
			advance();
		} else if (currentChar == '{') {
			returnToken = new Token(TokenTypes.LBRACKET, position);
			advance();
		} else if (currentChar == '}') {
			returnToken = new Token(TokenTypes.RBRACKET, position);
			advance();
		} else if (currentChar == '=') {
			returnToken = makeEqualsOrArrow();
		} else if (currentChar == '<') {
			returnToken = makeLessThan();
		} else if (currentChar == '>') {
			returnToken = makeGreaterThan();
		} else if (currentChar == '&') {
			returnToken = new Token(TokenTypes.AND, position);
			advance();
		} else if (currentChar == '|') {
			returnToken = new Token(TokenTypes.OR, position);
			advance();
		} else if (currentChar == '!') {
			returnToken = makeNot();
		} else if (currentChar == ':') {
			returnToken = new Token(TokenTypes.COLON, position);
			advance();
		} else if (currentChar == ',') {
			returnToken = new Token(TokenTypes.COMMA, position);
			advance();
		} else if (currentChar == '.') {
			returnToken = new Token(TokenTypes.PERIOD, position);
			advance();
		} else error = new LexError(position, "Illegal character '" + currentChar + "'");

		return returnToken;
	}

	/**
	 * Either makes a division token or skips a comment
	 *
	 * @return A token if it's a division, null if it's a comment
	 */

	private @Nullable Token makeDivOrComment() {
		Position startPosition = position.copy();
		advance();

		if (currentChar == '/') {
			while (!String.valueOf(currentChar).matches("[;\n]") || currentChar == Character.MIN_VALUE) {
				advance();
			}
			return null;
		} else if (currentChar == '*') {
			while (true) {
				advance();
				if (currentChar == '*') {
					advance();
					if (currentChar == '/') {
						advance();
						return null;
					}
				}
			}
		} else return new Token(TokenTypes.DIV, startPosition);
	}

	/**
	 * Makes a string token
	 *
	 * @return The string token
	 */

	@Contract(" -> new")
	private @NotNull Token makeString() {
		StringBuilder stringBuilder = new StringBuilder();
		Position startPosition = position.copy();
		boolean isEscapedCharacter = false;
		char character = currentChar;
		advance();

		while (currentChar != Character.MIN_VALUE && (currentChar != character || isEscapedCharacter)) {
			if (isEscapedCharacter) {
				if (Characters.escapedCharacters.containsKey(String.valueOf(currentChar)))
					stringBuilder.append(Characters.escapedCharacters.get(String.valueOf(currentChar))).append(Characters.escapedCharacters.get(String.valueOf(currentChar)));
				else stringBuilder.append(currentChar);
				isEscapedCharacter = false;
				advance();
			} else {
				if (currentChar == '\\' && nextChar != 'u') {
					isEscapedCharacter = true;
					advance();
				}
				if (!isEscapedCharacter) {
					if (character == '`' && currentChar == '$') {
						advance();
						if (currentChar == '{') {
							tokens.add(new Token(TokenTypes.STRING, parseUnicode(stringBuilder.toString()), startPosition, new Position(position.col - 2, position.line, position.index - 2, position.fileName, position.fileText)));
							tokens.add(new Token(TokenTypes.PLUS, position));
							advance();
							while (currentChar != '}') {
								Token token = makeToken();
								if (token != null) tokens.add(token);
							}
							tokens.add(new Token(TokenTypes.PLUS, position));

							stringBuilder = new StringBuilder();
							advance();
							startPosition = position;
						} else {
							stringBuilder.append('$').append(currentChar);
							advance();
						}
					} else {
						stringBuilder.append(currentChar);
						advance();
					}
				}
			}

		}
		advance();
		return new Token(TokenTypes.STRING, parseUnicode(stringBuilder.toString()), startPosition, position);
	}

	/**
	 * Makes a number token
	 *
	 * @return The number token
	 */

	@Contract(" -> new")
	private @NotNull Token makeNumber() {

		StringBuilder numberString = new StringBuilder();
		int dotCount = 0;

		Position startPosition = position.copy();

		while ((Characters.digits + ".").contains(String.valueOf(currentChar))) {
			if (currentChar == '.') {
				if (dotCount > 0) break;
				dotCount++;
				numberString.append(".");
			} else {
				numberString.append(currentChar);
			}
			advance();
		}

		return new Token(TokenTypes.NUMBER, numberString.toString(), startPosition, position);
	}

	/**
	 * Makes an identifier or a keyword token
	 *
	 * @return The identifier/keyword token
	 */

	@Contract(" -> new")
	private @NotNull Token makeIdentifier() {
		StringBuilder buildString = new StringBuilder();
		Position startPosition = position.copy();

		while (currentChar != Character.MIN_VALUE && Characters.lettersDigits.contains(String.valueOf(currentChar))) {
			buildString.append(currentChar);
			advance();
		}

		String identifierString = buildString.toString();

		TokenTypes tokenType;
		if (Arrays.asList(Characters.keywords).contains(identifierString)) {
			tokenType = TokenTypes.KEYWORD;
			if (identifierString.equals("else")) {
				Position position = this.position.copy();
				advance();
				if (currentChar == 'i') {
					advance();
					if (currentChar == 'f') {
						identifierString = "else if";
						advance();
					} else {
						this.position = position;
						if (position.index < code.length()) currentChar = code.charAt(position.index);
						else currentChar = Character.MIN_VALUE;
					}
				} else {
					this.position = position;
					if (position.index < code.length()) currentChar = code.charAt(position.index);
					else currentChar = Character.MIN_VALUE;
				}
			}
		} else tokenType = TokenTypes.IDENTIFIER;
		return new Token(tokenType, identifierString, startPosition, position);
	}

	/**
	 * Makes an equals or an arrow token
	 *
	 * @return The equals/arrow token
	 */

	@Contract(" -> new")
	private @NotNull Token makeEqualsOrArrow() {
		Position startPosition = position.copy();
		advance();

		if (currentChar == '=') {
			advance();
			return new Token(TokenTypes.EE, startPosition, position);
		} else if (currentChar == '>') {
			advance();
			return new Token(TokenTypes.ARROW, startPosition, position);
		} else return new Token(TokenTypes.EQ, startPosition);
	}

	/**
	 * Makes a not or a not equals token
	 *
	 * @return The not/not equals token
	 */

	@Contract(" -> new")
	private @NotNull Token makeNot() {
		Position startPosition = position.copy();
		advance();

		if (currentChar == '=') {
			advance();
			return new Token(TokenTypes.NE, startPosition, position);
		} else return new Token(TokenTypes.NOT, startPosition);
	}

	/**
	 * Makes a less than token
	 *
	 * @return The less than token
	 */

	@Contract(" -> new")
	private @NotNull Token makeLessThan() {
		Position startPosition = position.copy();
		advance();

		if (currentChar == '=') {
			advance();
			return new Token(TokenTypes.LTE, startPosition, position);
		} else return new Token(TokenTypes.LT, startPosition);
	}

	/**
	 * Makes a greater than token
	 *
	 * @return The greater than token
	 */

	@Contract(" -> new")
	private @NotNull Token makeGreaterThan() {
		Position startPosition = position.copy();
		advance();

		if (currentChar == '=') {
			advance();
			return new Token(TokenTypes.GTE, startPosition, position);
		} else return new Token(TokenTypes.GT, startPosition);
	}

	private @NotNull String parseUnicode(@NotNull String string) {
		Pattern regexPattern = Pattern.compile("\\\\u([\\da-zA-Z]{4})");
		Matcher regexMatcher = regexPattern.matcher(string);

		if (regexMatcher.find())
			return regexMatcher.replaceAll(String.valueOf((char) Integer.parseInt(regexMatcher.group(1), 16)));
		else return string;
	}
}
