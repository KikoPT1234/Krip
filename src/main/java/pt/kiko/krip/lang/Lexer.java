package pt.kiko.krip.lang;

import pt.kiko.krip.lang.errors.LexError;
import pt.kiko.krip.lang.results.LexResult;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

	String code;
	Position position;
	char currentChar;

	public Lexer(String code, String fileName) {
		this.code = code.replaceAll("\\r", "");
		position = new Position(-1, 0, -1, fileName, code);
		advance();
	}

	public void advance() {
		position.advance(currentChar);
		if (position.index < code.length()) {
			currentChar = code.charAt(position.index);
		} else currentChar = Character.MIN_VALUE;
	}

	public LexResult makeTokens() {

		ArrayList<Token> tokens = new ArrayList<>();

		while (position.index < code.length()) {

			Pattern newlinePattern = Pattern.compile("[;\n]");
			Matcher newlineMatcher = newlinePattern.matcher(String.valueOf(currentChar));

			Pattern whitespacePattern = Pattern.compile("\\s");
			Matcher whitespaceMatcher = whitespacePattern.matcher(String.valueOf(currentChar));

			if (newlineMatcher.matches()) {
				if (String.valueOf(currentChar).equals(";")) {
					Position startPosition = position.copy();
					advance();
					if (String.valueOf(currentChar).equals("\n")) {
						tokens.add(new Token(TokenTypes.NEWLINE, startPosition, position));
						advance();
					} else tokens.add(new Token(TokenTypes.NEWLINE, startPosition));
					continue;
				}
				tokens.add(new Token(TokenTypes.NEWLINE, position));
				advance();
			} else if (whitespaceMatcher.matches()) advance();
			else if (Characters.letters.contains(String.valueOf(currentChar))) {
				tokens.add(makeIdentifier());
			} else if (Characters.digits.contains(String.valueOf(currentChar))) {
				tokens.add(makeNumber());
			} else if (String.valueOf(currentChar).equals("\"")) {
				tokens.add(makeString());
			} else if (String.valueOf(currentChar).equals("+")) {
				tokens.add(new Token(TokenTypes.PLUS, position));
				advance();
			} else if (String.valueOf(currentChar).equals("-")) {
				tokens.add(new Token(TokenTypes.MINUS, position));
				advance();
			} else if (String.valueOf(currentChar).equals("*")) {
				tokens.add(new Token(TokenTypes.MUL, position));
				advance();
			} else if (String.valueOf(currentChar).equals("^")) {
				tokens.add(new Token(TokenTypes.POW, position));
				advance();
			} else if (String.valueOf(currentChar).equals("%")) {
				tokens.add(new Token(TokenTypes.MOD, position));
				advance();
			} else if (String.valueOf(currentChar).equals("/")) {
				tokens.add(new Token(TokenTypes.DIV, position));
				advance();
			} else if (String.valueOf(currentChar).equals("(")) {
				tokens.add(new Token(TokenTypes.LPAREN, position));
				advance();
			} else if (String.valueOf(currentChar).equals(")")) {
				tokens.add(new Token(TokenTypes.RPAREN, position));
				advance();
			} else if (String.valueOf(currentChar).equals("[")) {
				tokens.add(new Token(TokenTypes.LSQUARE, position));
				advance();
			} else if (String.valueOf(currentChar).equals("]")) {
				tokens.add(new Token(TokenTypes.RSQUARE, position));
				advance();
			} else if (String.valueOf(currentChar).equals("{")) {
				tokens.add(new Token(TokenTypes.LBRACKET, position));
				advance();
			} else if (String.valueOf(currentChar).equals("}")) {
				tokens.add(new Token(TokenTypes.RBRACKET, position));
				advance();
			} else if (String.valueOf(currentChar).equals("=")) {
				tokens.add(makeEqualsOrArrow());
			} else if (String.valueOf(currentChar).equals("<")) {
				tokens.add(makeLessThan());
			} else if (String.valueOf(currentChar).equals(">")) {
				tokens.add(makeGreaterThan());
			} else if (String.valueOf(currentChar).equals("&")) {
				tokens.add(new Token(TokenTypes.AND, position));
				advance();
			} else if (String.valueOf(currentChar).equals("|")) {
				tokens.add(new Token(TokenTypes.OR, position));
				advance();
			} else if (String.valueOf(currentChar).equals("!")) {
				tokens.add(makeNot());
			} else if (String.valueOf(currentChar).equals(":")) {
				tokens.add(new Token(TokenTypes.COLON, position));
				advance();
			} else if (String.valueOf(currentChar).equals(",")) {
				tokens.add(new Token(TokenTypes.COMMA, position));
				advance();
			} else if (String.valueOf(currentChar).equals(".")) {
				tokens.add(new Token(TokenTypes.PERIOD, position));
				advance();
			} else {
				return new LexResult().failure(new LexError(position, "Illegal character: '" + currentChar + "'"));
			}
		}

		tokens.add(new Token(TokenTypes.EOF, position));

		return new LexResult().success(tokens);

	}

	@Contract(" -> new")
	private @NotNull Token makeString() {
		StringBuilder stringBuilder = new StringBuilder();
		Position startPosition = position.copy();
		boolean isEscapedCharacter = false;
		advance();

		while (currentChar != Character.MIN_VALUE && (!String.valueOf(currentChar).equals("\"") || isEscapedCharacter)) {
			if (isEscapedCharacter) {
				if (Characters.escapedCharacters.containsKey(String.valueOf(currentChar))) stringBuilder.append(Characters.escapedCharacters.get(String.valueOf(currentChar)));
				else stringBuilder.append(currentChar);
			} else {
				if (String.valueOf(currentChar).equals("\\")) isEscapedCharacter = true;
				else stringBuilder.append(currentChar);
			}
			advance();
		}
		advance();
		return new Token(TokenTypes.STRING, stringBuilder.toString(), startPosition, position);
	}

	@Contract(" -> new")
	private @NotNull Token makeNumber() {

		StringBuilder numberString = new StringBuilder();
		int dotCount = 0;

		Position startPosition = position.copy();

		while ((Characters.digits + ".").contains(String.valueOf(currentChar))) {
			if (String.valueOf(currentChar).equals(".")) {
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
				if (String.valueOf(currentChar).equals("i")) {
					advance();
					if (String.valueOf(currentChar).equals("f")) {
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

	@Contract(" -> new")
	private @NotNull Token makeEqualsOrArrow() {
		Position startPosition = position.copy();
		advance();

		if (String.valueOf(currentChar).equals("=")) {
			advance();
			return new Token(TokenTypes.EE, startPosition, position);
		} else if (String.valueOf(currentChar).equals(">")) {
			advance();
			return new Token(TokenTypes.ARROW, startPosition, position);
		} else return new Token(TokenTypes.EQ, startPosition);
	}

	@Contract(" -> new")
	private @NotNull Token makeNot() {
		Position startPosition = position.copy();
		advance();

		if (String.valueOf(currentChar).equals("=")) {
			advance();
			return new Token(TokenTypes.NE, startPosition, position);
		} else return new Token(TokenTypes.NOT, startPosition);
	}

	@Contract(" -> new")
	private @NotNull Token makeLessThan() {
		Position startPosition = position.copy();
		advance();

		if (String.valueOf(currentChar).equals("=")) {
			advance();
			return new Token(TokenTypes.LTE, startPosition, position);
		} else return new Token(TokenTypes.LT, startPosition);
	}

	@Contract(" -> new")
	private @NotNull Token makeGreaterThan() {
		Position startPosition = position.copy();
		advance();

		if (String.valueOf(currentChar).equals("=")) {
			advance();
			return new Token(TokenTypes.GTE, startPosition, position);
		} else return new Token(TokenTypes.GT, startPosition);
	}
}
